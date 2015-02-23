package acceptance

import acceptance.helpers.ApplaudioAcceptance
import spray.http.MediaTypes._
import spray.http.StatusCodes._



class StaticResourcesAcceptanceSpec extends ApplaudioAcceptance {

  "Requests for the base path" should {
    "return index.html" in {
      Get("/") ~> routes ~> check {
        status === OK
        mediaType === `text/html`
      }
    }
  }

  "Requests for other static resources" should {
    "return a 200 response if they exist" in {
      Get("/main.js") ~> routes ~> check {
        status === OK
      }
    }
    "return a 400 if they do not exist" in {
      Get("/main.min.js") ~> sealRoute(routes) ~> check {
        status === NotFound
      }
    }
  }

}
