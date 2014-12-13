package applaudio.routing

import akka.actor.Actor
import applaudio.services.{AlbumService, TrackService, ArtistService}
import applaudio.slick.services.{SlickAlbumService, SlickTrackService, SlickArtistService}
import spray.httpx.encoding.Gzip
import spray.routing._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import scala.concurrent.ExecutionContext.Implicits.global


class Router extends Actor with ApplaudioRouting {
  def actorRefFactory = context
  override def receive: Receive = runRoute(routes)
}

trait ApplaudioRouting extends HttpService with ArtistsApi with AlbumsApi with TracksApi {

  val routes: Route = encodeResponse(Gzip) {
    pathPrefix("api") {
      respondWithMediaType(`application/json`) {
        artistRoutes ~
        albumRoutes ~
        trackRoutes
      }
    } ~
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Applaudio Angular Application</h1>
              </body>
            </html>
          }
        }
      }
    }
  }

}

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

trait TracksApi extends HttpService {

  val trackService: TrackService = new SlickTrackService

  val trackRoutes: Route = {
    path("tracks" / Segment) { artist =>
      get {
        complete {
          trackService.byArtist(artist)
        }
      }
    }
  }

}
