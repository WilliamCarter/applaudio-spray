package applaudio.utilities

import java.io.{FileOutputStream, File, FileInputStream}
import java.nio.charset.Charset
import java.nio.file.{Files, Paths, StandardCopyOption}

import org.jvnet.mimepull.{MIMEMessage, MIMEPart}
import spray.http.HttpHeaders.{RawHeader, `Content-Disposition`}
import spray.http.parser.HttpParser
import spray.http._
import spray.routing.Directive1
import spray.routing.Directives._

import scala.collection.JavaConverters._
import scala.io.Source

case class MultipartFormDataFilePart(name: String, contentType: String, data: File)
case class FileUploadFormData(file: Option[MultipartFormDataFilePart], fields: Map[String, String])

object FileUploadFormData {
  val empty = FileUploadFormData(None, Map.empty)
}

trait FileUpload {

  val UTF8 = Charset.forName("UTF-8")

  val tmpDirectory = Paths.get(System.getProperty("java.io.tmpdir")).toFile

  val ContentDispositionFilenameExtractor = """^filename=("?)(.*)\1$""".r


  val withMultipartFormData: Directive1[FileUploadFormData] = requestInstance.map { request: HttpRequest =>

    val Some(HttpHeaders.`Content-Type`(ContentType(multipart: MultipartMediaType, _))) = request.header[HttpHeaders.`Content-Type`]
    val boundary = multipart.parameters("boundary")

    StreamedRequestUploader.withUploadedRequestAsFile(request) { requestAsFile =>

      val requestMimeMessage = new MIMEMessage(new FileInputStream(requestAsFile), boundary)

      requestMimeMessage.getAttachments.asScala.foldLeft(FileUploadFormData.empty) { (formData, formPart) =>

        filenameForPart(formPart) match {
          case Some(filename) => {
            val uploadedFile = for {
              contentType <- contentTypeForPart(formPart)
            } yield {
              val tmpFile = new File(tmpDirectory, filename)
              Files.copy(formPart.readOnce(), Paths.get(tmpFile.getPath), StandardCopyOption.REPLACE_EXISTING)
              MultipartFormDataFilePart(filename, contentType, tmpFile)
            }

            FileUploadFormData(uploadedFile, formData.fields)
          }
          case None =>
            val nonFileField = nameForPart(formPart) -> Source.fromInputStream(formPart.readOnce).mkString
            FileUploadFormData(formData.file, formData.fields + nonFileField)
        }
      }
    }
  }


  private[utilities] def nameForPart(part: MIMEPart): String = {
    val dispHeader = part.getHeader("Content-Disposition").asScala.toSeq.head
    val Right(disp: `Content-Disposition`) = HttpParser.parseHeader(RawHeader("Content-Disposition", dispHeader))
    disp.parameters("name")
  }

  private[utilities] def filenameForPart(part: MIMEPart): Option[String] =
    part.getHeader("content-disposition").get(0).split("; ").collectFirst {
      case ContentDispositionFilenameExtractor(_, name) => name }

  private[utilities] def contentTypeForPart(part: MIMEPart): Option[String] =
    part.getHeader("content-type").asScala.headOption

}

object StreamedRequestUploader {

  lazy val MaxChunkSize = 1024 * 128 // get this from configuration

  private def uploadRequestToTempDirectory(request: HttpRequest): File = {
    val tmpFile = File.createTempFile("applaudio-upload", ".tmp")
    val output = new FileOutputStream(tmpFile)

    request.asPartStream(MaxChunkSize).foreach {
      case MessageChunk(data, extension) => output.write(data.toByteArray)
      case ChunkedMessageEnd => output.close()
      case _ => //do nothing
    }

    tmpFile
  }

  
  def withUploadedRequestAsFile[T](request:HttpRequest)(task: File => T): T = {
    val requestAsFile = uploadRequestToTempDirectory(request)
    try task(requestAsFile) finally requestAsFile.delete()
  }
}
