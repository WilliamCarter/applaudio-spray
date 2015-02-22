package acceptance

import akka.actor.ActorRefFactory
import applaudio.routing.ApplaudioRouting
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import spray.http.StatusCodes._
import spray.http._
import spray.testkit.Specs2RouteTest
import testutilities.MultipartFormBuilding


class TracksApiAcceptanceSpec extends Specification with Specs2RouteTest with ApplaudioRouting {
  def actorRefFactory: ActorRefFactory = system

//  "Requests for tracks" should {
//
//    "return a 200 response" in {
//      Get("/api/tracks") ~> routes ~> check {
//        status should be (OK)
//      }
//    }
//
//    "return a JSON response" in {
//      Get("/api/tracks") ~> routes ~> check {
//        mediaType should be (ContentTypes.`application/json`)
//      }
//    }
//  }
  
  "Track Upload" should {
    "return a 400 for requests without a title field" in new TestScope {
      uploadRequestWithoutTitle ~> routes ~> check {
        status should be (BadRequest)
      }
    }
  }

}

class TestScope extends Scope with MultipartFormBuilding {

  val uploadRequestWithoutTitle = multipartPost("/api/tracks/upload", formField("album", "13"), formField("encoding", "mp3"),
    fileField("/endless.mp3", MediaTypes.`audio/mpeg`))

}