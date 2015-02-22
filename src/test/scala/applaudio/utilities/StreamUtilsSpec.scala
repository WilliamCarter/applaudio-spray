//package applaudio.utilities
//
//import java.io.File
//
//import org.specs2.mutable.Specification
//import org.specs2.specification.Scope
//import spray.http._
//import spray.httpx.RequestBuilding
//import spray.routing.HttpService
//import spray.testkit.Specs2RouteTest
//
//class StreamUtilsSpec extends Specification with Specs2RouteTest {
//
////  "ContentDispositionExtractor" should {
////    val fileContentDisposition = "Content-Disposition: form-data; name=\"files[]\"; filename=\"Army of Me.mp3\""
////    "match on a content disposition string and return the parameters" in new ExampleStreams {
////      val MultipartContentDispositionExtractor(parameters) = fileContentDisposition
////      parameters must be equalTo "name=\"files[]\"; filename=\"Army of Me.mp3\""
////    }
////    "return a content disposition object" in new Scope with StreamUtils {
////      val contentDisposition = multipartContentDisposition("name=\"files[]\"; filename=\"Army of Me.mp3\"")
////      println(contentDisposition)
////      contentDisposition.dispositionType must be equalTo "form-data"
////      contentDisposition.parameters must have size 2
////      contentDisposition.parameters("name") must be equalTo "files[]"
////      contentDisposition.parameters("filename") must be equalTo "Army of Me.mp3"
////    }
////  }
////
////  "splitChunkedStream" should {
////    "split stream items that contain the separator" in new ExampleStreams {
////      splitChunkedStream('|')(chunkedSentence).toList.filterNot(_.isEmpty) must be equalTo
////        "words in this sentence are pipe separated".split(" ").toList
////    }
////    "not exceed the current chunk size" in new ExampleStreams {
////      splitChunkedStream('|')(chunkedFileRequest).exists(_.size > 16) must beFalse
////    }
////  }
////
////  "withCompleteBoundaries" should {
////    "reattach stream boundaries that may have been lost in the chunking process" in new ExampleStreams {
////      val stream = List("boun", "dary", "content", "boundary", "content", "bounda", "ry--").toStream
////      withCompleteBoundaries("boundary")(stream).toList must be equalTo
////        "boundary content boundary content boundary--".split(" ").toList
////    }
////    "should always return the complete boundary if it exists at the head of the stream" in new ExampleStreams {
////      val stream = List("bo", "un", "da", "ry", "content").toStream
////      withCompleteBoundaries("boundary")(stream).head must be equalTo "boundary"
////    }
////  }
//
////  "formDataStream" should {
////    "pull the form fields out as map keys" in new ExampleStreams {
////      bjorkFormStream must have size (2)
////      bjorkFormStream.keys must containAllOf (List("title", "artist"))
////    }
////    "pull the data fields out as streams" in new ExampleStreams {
////      bjorkFormStream("artist").mkString must be equalTo "Bjork"
////      bjorkFormStream("title").mkString must be equalTo "Army Of Me"
////    }
////  }
//
////  "formDataFromStream" should {
////    "extract all non file items" in new ExampleStreams {
////      formDataFromStream(bjorkFormStream)._2 must be equalTo Map(
////        "artist" -> "Bjork",
////        "title" -> "Army Of Me"
////      )
////    }
////    "return a None if no file data is given in the request" in new ExampleStreams {
////      formDataFromStream(bjorkFormStream)._1 must beNone
////    }
////    "save file data to a temporary file if it exists" in new ExampleStreams {
////      val streamMap = bjorkFormStreamWithFile
////      println("----------------------")
////      println(streamMap)
////      println("STARTING THE TEST NOW")
////
////      formDataFromStream(streamMap)._1 must beSome
////
////    }
////  }
//
//}
//
//class ExampleStreams extends Scope with StreamUtils {
//  val chunkedSentence = List("|words|in|", "this|sen", "tence|ar", "e|pipe|se", "parated|").toStream
//  val chunkedFileRequest = List("file|---", "--------", "--------", "---|done").toStream
//
//  val bjorkMultiPartFormStream =
//    """
//      |----WebKitFormBoundaryE19zNvXGzXaLvS5C
//      |Content-Disposition: form-data; name="title"
//      |Content-Type: text/plain; charset=UTF-8
//      |
//      |Army Of Me
//      |----WebKitFormBoundaryE19zNvXGzXaLvS5C
//      |Content-Disposition: form-data; name="artist"
//      |Content-Type: text/plain; charset=UTF-8
//      |
//      |Bjork
//      |----WebKitFormBoundaryE19zNvXGzXaLvS5C
//      |
//    """.stripMargin.lines.toStream
//
//  val multiPartFormDataFile =
//    """
//      |Content-Disposition: form-data; name="files[]"; filename="Army of Me.mp3"
//      |Content-Type: audio/mp3
//      |
//      |fakefiledatafakefiledata
//      |----WebKitFormBoundaryE19zNvXGzXaLvS5C
//    """.stripMargin.lines.toStream
//
////  val bjorkFormStream: Map[String, Stream[String]] = formDataStream(bjorkMultiPartFormStream, "----WebKitFormBoundaryE19zNvXGzXaLvS5C")
////  val bjorkFormStreamWithFile: Map[String, Stream[String]] =
////    formDataStream(bjorkMultiPartFormStream append multiPartFormDataFile, "----WebKitFormBoundaryE19zNvXGzXaLvS5C")
//
//}
//
//class StreamUtilsIntegrationSpec extends Specification with HttpService with Specs2RouteTest with StreamUtils {
//  def actorRefFactory = system
//
//  "withMultipartFormData" should {
////    "extract form data from the request" in new ExampleRequests {
////      multipartRequestWithParts(formFieldBodyPart) ~> withMultipartFormUpload { uploadData => complete(uploadData.fields("key")) } ~> check {
////        status === StatusCodes.OK
////        responseAs[String] === "value"
////      }
////    }
//    "extract file data from the request" in new ExampleRequests{
//      multipartRequestWithParts(fileBodyPart) ~> withMultipartFormUpload { uploadData => complete(uploadData.files.head.getName) } ~> check {
//        status === StatusCodes.OK
//        responseAs[String] must startWith("applaudio-upload")
//      }
//    }
//  }
//
//
//
//}
//
//class ExampleRequests extends Scope with RequestBuilding {
//  val endlessMp3 = new File(getClass.getResource("/endless.mp3").toURI)
//  val formFieldBodyPart = BodyPart(HttpEntity("value"), "key")
//  val fileBodyPart = BodyPart(endlessMp3, "files[]", ContentType(MediaTypes.`audio/mpeg`))
//
//  def multipartRequestWithParts(parts: BodyPart*): HttpRequest = {
//    val request = Post("/postendpoint", MultipartFormData(parts))
//    val HttpEntity.NonEmpty(ContentType(multipart: MultipartMediaType, _), _) = request.entity
//    request.withHeaders(HttpHeaders.`Content-Type`(ContentType(multipart)))
//  }
//}
