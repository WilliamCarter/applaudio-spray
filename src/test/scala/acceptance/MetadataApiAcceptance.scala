package acceptance

import acceptance.helpers.ApplaudioAcceptance
import applaudio.models.Track
import org.specs2.specification.Scope
import spray.http.StatusCodes._
import spray.http.MediaTypes
import spray.httpx.SprayJsonSupport._

import testutilities.MultipartFormBuilding

class MetadataApiAcceptance extends ApplaudioAcceptance {

  "Requests for file metadata" should {
    "return a 400 Bad Request response if request does not contain a file" in new MetadataRequests {

    }
    "return a 200 response of content type JSON for valid requests" in new MetadataRequests {
      badMetadataRequest ~> routes ~> check {
        status must be equalTo BadRequest
      }
    }
    "return a Track representation of the posted file" in new MetadataRequests {
      metadataRequest ~> unzippedRoutes ~> check {
        println(status)
        println(body)
        responseAs[Track] must be equalTo coverMeMetadata
      }
    }
  }
}

trait MetadataRequests extends Scope with MultipartFormBuilding {

  val badMetadataRequest = multipartPost("/api/metadata", formField("artist", "Björk"))
  val metadataRequest = multipartPost("/api/metadata", fileField("/Cover Me.mp3", MediaTypes.`audio/mpeg`))

  val coverMeMetadata = Track("Cover Me", Some("Björk"), Some("Post"), Some(10), None, Some(1995), "mp3")
}
