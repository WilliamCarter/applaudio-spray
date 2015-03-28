package applaudio.routing

import applaudio.services.AlbumService
import spray.httpx.SprayJsonSupport._
import spray.routing.{HttpService, Route}

import scala.concurrent.ExecutionContext.Implicits.global

trait AlbumsApi extends HttpService {

  def albumService: AlbumService

  val albumRoutes: Route = {
    path("albums" / Segment) { artist =>
      get {
        complete {
          albumService.byArtist(artist)
        }
      }
    }
  }

}
