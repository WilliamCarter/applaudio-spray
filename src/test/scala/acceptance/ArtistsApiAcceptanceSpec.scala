package acceptance

import acceptance.helpers.{ApplaudioAcceptance, FakeDatabase}
import spray.http.StatusCodes._


class ArtistsApiAcceptanceSpec extends ApplaudioAcceptance {

  "Requests for artists" should {

    "return a 200 response" in new FakeDatabase {
      Get("/api/artists") ~> routes ~> check {
        status should be(OK)
      }
    }

    "return a JSON response" in new FakeDatabase {
      Get("/api/artists") ~> routes ~> check {
        mediaType.toString must contain ("application/json")
      }
    }

    "return all artists in the database" in new FakeDatabase {
      Get("/api/artists") ~> routes ~> check {
        status === OK
      }
    }
  }

}
