package testutilities

import spray.http._
import spray.httpx.RequestBuilding

trait MultipartFormBuilding extends RequestBuilding with TestResources {

  def formField(key: String, value: String) = BodyPart(HttpEntity(value), key)
  def fileField(resource: String, contentType: ContentType) = {
    BodyPart(getResource(resource), "files[]", contentType)
  }

  def multipartPost(url: String, parts: BodyPart*): HttpRequest = {
    val postRequest = Post(url, MultipartFormData(parts))
    val HttpEntity.NonEmpty(ContentType(multipart: MultipartMediaType, _), _) = postRequest.entity
    postRequest.withHeaders(HttpHeaders.`Content-Type`(ContentType(multipart)))
  }

}
