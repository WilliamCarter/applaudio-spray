package acceptance

import acceptance.helpers.{ApplaudioAcceptance, FakeDatabase}
import applaudio.models.Track
import org.specs2.specification.Scope
import spray.http.StatusCodes._
import spray.http._
import spray.httpx.SprayJsonSupport._
import testutilities.MultipartFormBuilding


class TracksApiAcceptanceSpec extends ApplaudioAcceptance {


  "Requests for tracks by artist" should {

    "return a 200 response" in new FakeDatabase {
      Get("/api/tracks/Blur") ~> routes ~> check {
        status should be (OK)
      }
    }

    "return a JSON response" in new Thirteen {
      Get("/api/tracks/Blur") ~> routes ~> check {
        contentType === ContentTypes.`application/json`
      }
    }

    "return a list of all relevant track objects from the database" in new Thirteen {
      Get("/api/tracks/Blur") ~> unzippedRoutes ~> check {
        responseAs[List[Track]].head.title must be equalTo "Tender"
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
  }

}

trait Thirteen extends FakeDatabase {
  override def bemore = insertTrack("Tender", "Blur", "13", length = 7 * 60 + 40, year = 1999)
}

trait UploadRequests extends Scope with MultipartFormBuilding {

  val uploadRequestWithoutTitle = multipartPost("/api/tracks/upload", formField("album", "13"), formField("encoding", "mp3"),
    fileField("/endless.mp3", MediaTypes.`audio/mpeg`))

  val uploadRequestWithoutEncoding = multipartPost("/api/tracks/upload", formField("artist", "Blur"), formField("album", "13"),
    fileField("/endless.mp3", MediaTypes.`audio/mpeg`))

}
