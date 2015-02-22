package applaudio.utilities

import java.io.{ByteArrayInputStream, File}

import org.jvnet.mimepull.MIMEMessage
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import spray.http._
import spray.httpx.RequestBuilding
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

class FileUploadSpec extends Specification with HttpService with Specs2RouteTest {
  def actorRefFactory = system

  "nameForPart" should {
    "extract the form field name from the given form data body part" in new FormExamples {
      nameForPart(stringMimePart) must be equalTo "key"
    }
  }

  "filenameForPart" should {
    "FormPartFilenameExtractor" should {
      "extract the filename from the content disposition header when in quotes" in new FormExamples {
        val ContentDispositionFilenameExtractor(maybeQuote, filename) = "filename=\"endless.mp3\""
        filename must be equalTo "endless.mp3"
      }
      "extract the filename from the content disposition header when not in quotes" in new FormExamples {
        val ContentDispositionFilenameExtractor(maybeQuote, filename) = "filename=endless.mp3"
        filename must be equalTo "endless.mp3"
      }
    }
    "extract the form field name from the given form data body part" in new FormExamples {
      filenameForPart(fileMimePart) must beSome ("endless.mp3")
    }
  }

  "contentTypeForPart" should {
    "extract the form field name from the given form data body part" in new FormExamples {
      contentTypeForPart(fileMimePart) must beSome ("audio/mpeg")
    }
  }

  "withMultipartFormData directive" should {
    "extract all form fields from the request body" in new FormExamples {
      multipartPost(formFieldBodyPart) ~> withMultipartFormData (data => complete (data.fields("key"))) ~> check {
        responseAs[String] === "value"
      }
    }
    "extract file form fields from the request body" in new FormExamples {
      multipartPost(fileBodyPart) ~> withMultipartFormData (data => complete (data.file.get.name)) ~> check {
        responseAs[String] === endlessMp3.getName
      }
    }
  }


}

class FormExamples extends Scope with FileUpload with RequestBuilding {

  val endlessMp3 = new File(getClass.getResource("/endless.mp3").toURI)
  val formFieldBodyPart = BodyPart(HttpEntity("value"), "key")
  val fileBodyPart = BodyPart(endlessMp3, "files[]", ContentType(MediaTypes.`audio/mpeg`))

  def multipartPost(parts: BodyPart*): HttpRequest = {
    val postRequest = Post("/postendpoint", MultipartFormData(parts))
    val HttpEntity.NonEmpty(ContentType(multipart: MultipartMediaType, _), _) = postRequest.entity
    postRequest.withHeaders(HttpHeaders.`Content-Type`(ContentType(multipart)))
  }

  lazy val multipartPostAsMimeMessage = {
    val request = multipartPost(formFieldBodyPart, fileBodyPart)
    val HttpEntity.NonEmpty(ContentType(multipart: MultipartMediaType, _), _) = request.entity

    new MIMEMessage(new ByteArrayInputStream(request.entity.data.asString(UTF8).getBytes(UTF8)), multipart.parameters("boundary"))
  }

  lazy val stringMimePart = multipartPostAsMimeMessage.getAttachments.get(0)
  lazy val fileMimePart = multipartPostAsMimeMessage.getAttachments.get(1)

}
