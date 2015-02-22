package applaudio.utilities

import java.io.{FileOutputStream, File}
import java.nio.charset.Charset

import spray.http._
import spray.http.HttpHeaders.`Content-Disposition`
import spray.routing.Directive1
import spray.routing.Directives._

/*
    Alternative to FileUpload.scala, using scala streams instead of the MIMEPull library.
    Difficult to parse headers correctly and robustly so using MIMEPull until bugs are removed
 */



trait StreamUtils {

  import applaudio.utilities.StringExtensions._

  val MaxChunkSize = 256
  val UTF8 = Charset.forName("UTF-8")
  val MultipartFileTypeName = "files[]"

  val MultipartContentDispositionExtractor = """^Content-Disposition: form-data; (.*)""".r
  private[utilities] def multipartContentDisposition(allParameters: String): `Content-Disposition` = {
    val parameters: Array[(String, String)] = allParameters.split("; ").map { parameter =>
      (parameter.takeWhile(_ != '='), parameter.dropWhile(_ != '=').tail.trim('"'))
    }
    `Content-Disposition`("form-data", parameters.toMap)
  }

  def splitChunkedStream(separator: Char)(chunkedStream: Stream[String]): Stream[String] = (chunkedStream match {
    case head #:: rest if head.contains(separator) => {
      println("### splitChunkedStream match ###")
      println("head #:: rest if head.contains(separator)")
      val (headBeforeNewline, headAfterNewline) = head.partitionAtFirst(separator)
      println(s"|${headBeforeNewline.trim}|")
      println(s"|${headAfterNewline.trim}|")
      headBeforeNewline.trim #:: splitChunkedStream(separator)(headAfterNewline.trim #:: rest)
    }
    case head #:: rest => {
      println("### splitChunkedStream match ###")
      println("head #:: rest")
      head.trim #:: splitChunkedStream(separator)(rest)
    }
    case stream => {
      println("### splitChunkedStream match ###")
      println("stream")
      println(stream.mkString("|"))
      stream
    }
  }).filterNot(_.isEmpty)

  def chunkedStreamByLine = splitChunkedStream('\n') _

  private[utilities] def withCompleteBoundaries(boundary: String)(chunkedStream: Stream[String]): Stream[String] = chunkedStream match {
    case head #:: next #:: rest if boundary.startsWith(head + next) || s"$boundary--".startsWith(head + next) =>
      withCompleteBoundaries(boundary)((head + next) #:: rest)
    case head #:: rest => head #:: withCompleteBoundaries(boundary)(rest)
    case other => other
  }

  def formDataStream(rawStreamWithBoundaries: Stream[String], boundary: String): MultipartFormUploadData = {

    def isBoundary(line: String) = line.contains(boundary)
    def isContentType(line: String) = line.startsWith("Content-Type:")

    def formDataStreamIteration(input: Stream[String], acc: MultipartFormUploadData): MultipartFormUploadData = {
      input match {
        case MultipartContentDispositionExtractor(headers) #:: rest => {
          val contentDisposition = multipartContentDisposition(headers)
          contentDisposition.parameters("name") match {
            case MultipartFileTypeName => {
              println("*** Got a file here ***")
              val tmpFile = File.createTempFile("applaudio-upload", ".tmp")
              val output = new FileOutputStream(tmpFile)

              def writeToFile(stream: Stream[String]): Stream[String] = {
                println("write to file")
                println(stream.mkString("|"))
                stream match {
                  case line #:: more if isContentType(line) => writeToFile(more)
                  case line #:: more if !isBoundary(line) => {
                    println("match: line #:: more not boundary and not content type")
                    output.write(line.getBytes(UTF8))
                    writeToFile(more)
                  }
                  case more => output.close(); more
                }
              }

              val remainingStream = writeToFile(rest)
              println
              formDataStreamIteration(remainingStream, acc.copy(files = tmpFile :: acc.files))
            }
            case field => {
//              println(s"Extracting form field: $field")
              val (data, otherFields) = rest.span(!isBoundary(_))
//              println(data.mkString("|"))
//              println(data.filter(isContentType).mkString("|"))
//              println(otherFields.mkString("|"))
              val formField = field -> data.filterNot(isContentType).mkString
              formDataStreamIteration(otherFields.tail, acc.copy(fields = acc.fields + formField))
            }
          }
        }
        case nonContentDisposition #:: rest => formDataStreamIteration(rest, acc)
        case Stream.Empty => acc
      }
    }

    formDataStreamIteration(rawStreamWithBoundaries, MultipartFormUploadData.Empty)
  }

//  def formDataStream(formStream: Stream[String], boundary: String): Map[String, Stream[String]] = {
//    println(s"formDataStream($formStream, $boundary)")
//    println(formStream.mkString("|"))
////    val ContentDispositionRegex = """^Content-Disposition: form-data; name=("?)(.*)\1""".r
//    def isContentType(line: String) = line.startsWith("Content-Type:")
//    def isContentDisposition(line: String) = line.startsWith("Content-Type:")
//    def isNotBoundaryLine(line: String) = !line.contains(boundary)
//    def isBoundary(line: String): Boolean = line == boundary
////    def possibleContentDisposition(line: String) = "Content-Disposition: form-data".startsWith(line)
//
//    def formDataStreamIteration(input: Stream[String], acc: Map[String, Stream[String]]): Map[String, Stream[String]] = {
//      println("formDataStreamIteration")
//      input match {
////        case cutOffLine #:: next #:: rest if possibleContentDisposition(cutOffLine) && !next.isEmpty => {
////          println(s"possibleContentDisposition - $cutOffLine #:: $next #:: $rest")
////          formDataStreamIteration((cutOffLine + next) #:: rest, acc)
////        }
//        case MultipartContentDispositionExtractor(parametersString) #:: rest => {
//          val field = multipartContentDisposition(parametersString).parameters("name")
//          println(s"this line matches the content disposition bit with field: $field")
//          formDataStreamIteration(rest, acc ++ Map(field -> rest.filterNot(isContentType).takeWhile(isNotBoundaryLine)))
//        }
//        case notAFormField #:: rest => {
//          println(s"non matching line: |$notAFormField|")
//          println(notAFormField.length)
//          formDataStreamIteration(rest, acc)
//        }
//        case Stream.Empty => acc
//      }
//    }
//
//    formDataStreamIteration(formStream, Map.empty)
//  }
//
//  def formDataFromStream(formDataStream: Map[String, Stream[String]]): MultipartFormUploadData = {
//    formDataStream.foldLeft[MultipartFormUploadData](MultipartFormUploadData.Empty) {
//      case (MultipartFormUploadData(files, formFields), ("files[]", fileStream)) => {
//        val tmpDirectory = Paths.get(System.getProperty("java.io.tmpdir")).toFile
//        val tmpFile = File.createTempFile("chunked-receiver", ".tmp", tmpDirectory)
//        val output = new FileOutputStream(tmpFile)
//
//        fileStream.foreach(line => output.write(line.getBytes(UTF8)))
//        output.close()
//
//        MultipartFormUploadData(tmpFile :: files, formFields)
//      }
//      case (MultipartFormUploadData(files, formFields), (key, valueStream)) =>
//        MultipartFormUploadData(files, formFields + (key -> valueStream.mkString))
//    }
//  }

  val withMultipartFormUpload: Directive1[MultipartFormUploadData] = requestInstance.map { request: HttpRequest =>
    println("withMultipartFormUpload")
    val Some(HttpHeaders.`Content-Type`(ContentType(multipart: MultipartMediaType, contentTypeCharset: Option[HttpCharset]))) =
      request.header[HttpHeaders.`Content-Type`]
    val boundary = "--" + multipart.parameters("boundary")
    val charset = contentTypeCharset.map(_.nioCharset).getOrElse(UTF8)
    println(s"boundary - |$boundary|")
    println(s"charset - |$charset|")

    val chunkedRequestStream = request.asPartStream(MaxChunkSize).map {
      case MessageChunk(data, extension) => data.asString(charset)
      case _ => ""
    }
    println
    println("chunkedRequestStream")
    println(chunkedRequestStream.mkString("|"))

    val lineRequestStream = chunkedStreamByLine(chunkedRequestStream)
    println
    println("lineRequestStream")
    println(lineRequestStream.mkString("|"))

    val encodedRequestStream = withCompleteBoundaries(boundary){ lineRequestStream }
    println
    println("encodedRequestStream")
    println(encodedRequestStream.mkString("|"))

    println

    val multipartData = formDataStream(encodedRequestStream, boundary)
    println(multipartData)
    multipartData
  }

//  def withMultipartFormData(block: (Option[File], Map[String, String]) => StandardRoute)(implicit request: HttpRequest): StandardRoute = {
//
//    val Some(HttpHeaders.`Content-Type`(ContentType(multipart: MultipartMediaType, _))) = request.header[HttpHeaders.`Content-Type`]
//    val boundary = multipart.parameters("boundary")
//
//    val requestDataStream: Stream[String] = request.asPartStream(MaxChunkSize).map {
//      case MessageChunk(data, extension) => {
//        println("------------------------------------------------")
//        println("Received message chunk of length: " + data.length)
//        println("------------------------------------------------")
//        data.asString(UTF8)
//      }
//      case _ => ""
//    }
//
//    println("=================================")
//    println(requestDataStream.mkString("|"))
//    val lineStream = chunkedStreamByLine(requestDataStream)
//    println("=================================")
//    println(lineStream.mkString("|"))
//    println("=================================")
//    val formFields = formDataStream(lineStream, boundary)
//    println(formFields)
//    println("=================================")
//
//    block(None, formFields.mapValues(_.mkString))
//  }

}

case class MultipartFormUploadData(files: List[File], fields: Map[String, String])
object MultipartFormUploadData {
  val Empty = apply(Nil, Map.empty)
}

