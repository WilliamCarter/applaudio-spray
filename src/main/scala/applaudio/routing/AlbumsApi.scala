package applaudio.routing

import applaudio.services.AlbumService
import applaudio.persistence.services.SlickAlbumService
import spray.routing.{Route, HttpService}
import spray.httpx.SprayJsonSupport._
import scala.concurrent.ExecutionContext.Implicits.global

trait AlbumsApi extends HttpService {

  val albumService: AlbumService = new SlickAlbumService

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
