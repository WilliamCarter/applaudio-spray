package acceptance

import akka.actor.ActorRefFactory
import applaudio.models.Artist
import applaudio.routing.ApplaudioRouting
import org.specs2.mutable.Specification
import spray.httpx.encoding.Gzip
import spray.testkit.Specs2RouteTest
import spray.http.StatusCodes._
import spray.http.MediaTypes._


class ArtistsSpec extends Specification with Specs2RouteTest with ApplaudioRouting {
  def actorRefFactory: ActorRefFactory = system

  "Requests for artists" should {

    "return a 200 response" in {
      Get("/api/artists") ~> routes ~> check {
        status should be (OK)
      }
    }

    "return a JSON response" in {
      Get("/api/artists") ~> routes ~> check {
        mediaType should be (`application/json`)
      }
    }

    "return all artists in the database" in {
      Get("/api/artists") ~> routes ~> check {
        println(Gzip.decode(response).entity.asString)
        status === OK
      }
    }
  }

}
