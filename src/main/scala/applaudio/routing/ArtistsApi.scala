package applaudio.routing

import applaudio.services.ArtistService
import spray.httpx.SprayJsonSupport._
import spray.routing.{HttpService, Route}

import scala.concurrent.ExecutionContext.Implicits.global

trait ArtistsApi extends HttpService {

  def artistService: ArtistService

  val artistRoutes: Route = {
    path("artists") {
      get {
        complete {
          artistService.all
        }
      }
    }
  }

}
