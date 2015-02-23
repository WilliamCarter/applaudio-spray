package acceptance

import acceptance.helpers.{ApplaudioAcceptance, FakeDatabase, Thirteen}
import applaudio.models.Artist
import spray.httpx.SprayJsonSupport._


class ArtistsApiAcceptanceSpec extends ApplaudioAcceptance {

  "Requests for artists" should {

    "return a 200 response" in new FakeDatabase {
      Get("/api/artists") ~> routes ~> assertJsonOk
    }

    "return all artists in the database" in new Thirteen {
      Get("/api/artists") ~> unzippedRoutes ~> check {
        responseAs[List[Artist]] must have size 1
      }
    }
  }

}
