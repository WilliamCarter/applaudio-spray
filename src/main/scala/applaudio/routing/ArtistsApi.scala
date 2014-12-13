package applaudio.routing

import applaudio.services.ArtistService
import applaudio.slick.services.SlickArtistService
import spray.routing.{Route, HttpService}
import spray.httpx.SprayJsonSupport._
import scala.concurrent.ExecutionContext.Implicits.global

trait ArtistsApi extends HttpService {

  val artistService: ArtistService = new SlickArtistService

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
