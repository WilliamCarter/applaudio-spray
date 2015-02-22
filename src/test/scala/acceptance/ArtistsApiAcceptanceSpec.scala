package acceptance

import akka.actor.ActorRefFactory
import applaudio.routing.ApplaudioRouting
import org.specs2.mutable.Specification
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.testkit.Specs2RouteTest


class ArtistsApiAcceptanceSpec extends Specification with Specs2RouteTest with ApplaudioRouting {
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
        status === OK
      }
    }
  }

}
