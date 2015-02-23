package acceptance

import acceptance.helpers.{Thirteen, ApplaudioAcceptance, FakeDatabase}
import applaudio.models.Track
import org.specs2.specification.Scope
import spray.http.StatusCodes._
import spray.http._
import spray.httpx.SprayJsonSupport._
import testutilities.MultipartFormBuilding


class TracksApiAcceptanceSpec extends ApplaudioAcceptance {


  "Requests for tracks by artist" should {
    "return a 200 response of content type JSON" in new FakeDatabase {
      Get("/api/tracks/Blur") ~> routes ~> assertJsonOk
    }
    "return a list of all relevant track objects from the database" in new Thirteen {
      Get("/api/tracks/Blur") ~> unzippedRoutes ~> check {
        responseAs[List[Track]].head.artist must beSome ("Blur")
      }
    }
    "return an empty array if no tracks with the given artist exist" in new Thirteen {
      Get("/api/tracks/Iron%20Maiden") ~> unzippedRoutes ~> check {
        responseAs[List[Track]] must be empty
      }
    }
  }

  "Requests for tracks by album" should {
    "return a 200 response of content type JSON" in new FakeDatabase {
      Get("/api/tracks/Blur/13") ~> routes ~> assertJsonOk
    }
    "return a list of all relevant track objects from the database" in new Thirteen {
      Get("/api/tracks/Blur/13") ~> unzippedRoutes ~> check {
        responseAs[List[Track]].head.artist must beSome ("Blur")
        responseAs[List[Track]].head.album must beSome ("13")
      }
    }
    "return an empty array if no tracks with the given artist exist" in new Thirteen {
      Get("/api/tracks/Blur/Leisure") ~> unzippedRoutes ~> check {
        responseAs[List[Track]] must be empty
      }
    }
  }

  "Track Upload" should {
    "return a 400 for requests without a title field" in new UploadRequests {
      uploadRequestWithoutTitle ~> routes ~> check {
        status should be (BadRequest)
      }
    }
    "return a 400 for requests without an encoding field" in new UploadRequests {
      uploadRequestWithoutEncoding ~> routes ~> check {
        status should be (BadRequest)
      }
    }
    "return a 200 response of content type JSON" in new UploadRequests with FakeDatabase {
      validUploadRequest ~> routes ~> assertJsonOk
    }
    "return the newly created resource with a unique id" in new UploadRequests with FakeDatabase {
      validUploadRequest ~> unzippedRoutes ~> check {
        responseAs[Track].title must be equalTo "Caramel"
        responseAs[Track].id must beSome
      }
      Get("/api/tracks/Blur") ~> unzippedRoutes ~> check {
        responseAs[List[Track]].head.title must be equalTo "Caramel"
      }

    }
  }

}

trait UploadRequests extends Scope with MultipartFormBuilding {

  val uploadRequestWithoutTitle = multipartPost("/api/tracks/upload", formField("album", "13"), formField("encoding", "mp3"),
    fileField("/endless.mp3", MediaTypes.`audio/mpeg`))

  val uploadRequestWithoutEncoding = multipartPost("/api/tracks/upload", formField("title", "Blur"), formField("artist", "Blur"),
    formField("album", "13"), fileField("/endless.mp3", MediaTypes.`audio/mpeg`))

  val validUploadRequest = multipartPost("/api/tracks/upload", formField("title", "Caramel"), formField("artist", "Blur"),
    formField("album", "13"), formField("encoding", "mp3"), fileField("/endless.mp3", MediaTypes.`audio/mpeg`))
}
