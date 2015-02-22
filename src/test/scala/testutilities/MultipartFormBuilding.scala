package testutilities

import java.io.File

import spray.http._
import spray.httpx.RequestBuilding

trait MultipartFormBuilding extends RequestBuilding {

  def formField(key: String, value: String) = BodyPart(HttpEntity(value), key)
  def fileField(resourcePath: String, contentType: ContentType) = {
    val file = new File(getClass.getResource(resourcePath).toURI)
    BodyPart(file, "files[]", contentType)
  }

  def multipartPost(url: String, parts: BodyPart*): HttpRequest = {
    val postRequest = Post(url, MultipartFormData(parts))
    val HttpEntity.NonEmpty(ContentType(multipart: MultipartMediaType, _), _) = postRequest.entity
    postRequest.withHeaders(HttpHeaders.`Content-Type`(ContentType(multipart)))
  }

}
