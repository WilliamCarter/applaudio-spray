package acceptance

import acceptance.helpers.{ApplaudioAcceptance, FakeDatabase, Thirteen}
import applaudio.models.Album
import spray.httpx.SprayJsonSupport._


class AlbumsApiAcceptanceSpec extends ApplaudioAcceptance {

  "Requests for albums" should {

    "return a 200 response of content type JSON" in new FakeDatabase {
      Get("/api/albums/Blur") ~> routes ~> assertJsonOk
    }
    "return all albums in the database" in new Thirteen {
      Get("/api/albums/Blur") ~> unzippedRoutes ~> check {
        responseAs[List[Album]].head.name must be equalTo "13"
      }
    }
    "return an empty array if no albums by the given artist exist" in new Thirteen {
      Get("/api/albums/Iron%20Maiden") ~> unzippedRoutes ~> check {
        responseAs[List[Album]] must be empty
      }
    }

  }

}
