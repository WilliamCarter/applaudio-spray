package applaudio.routing

import akka.actor.Actor
import applaudio.error.{DatabaseError, LibraryError, RequestError}
import applaudio.persistence.library.DefaultLibraryService
import applaudio.persistence.services.{SlickAlbumService, SlickArtistService, SlickTrackService}
import applaudio.services.{AlbumService, ArtistService, LibraryService, TrackService}
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.encoding.Gzip
import spray.routing._
import spray.util.LoggingContext


class Router extends Actor with ApplaudioRouting {
  def actorRefFactory = context
  override def receive: Receive = runRoute(routes)
}

trait ApplaudioRouting extends HttpService with ArtistsApi with AlbumsApi with TracksApi {

  val libraryService: LibraryService = new DefaultLibraryService
  val artistService: ArtistService = new SlickArtistService
  val albumService: AlbumService = new SlickAlbumService
  val trackService: TrackService = new SlickTrackService

  val routes: Route = {
    path("library" / Segment) { filename =>
      getFromFile(libraryService.get(filename))
    } ~
    encodeResponse(Gzip) {
      pathPrefix("api") {
        handleExceptions(apiErrorHandler) {
          respondWithMediaType(`application/json`) {
            artistRoutes ~
              albumRoutes ~
              trackRoutes
          }
        }
      } ~
      path(Rest) {
        case "" => getFromResource("app/index.html")
        case staticResource => getFromResource(s"app/$staticResource")
      }
    }
  }

  def apiErrorHandler(implicit log: LoggingContext) = ExceptionHandler {
    case e: DatabaseError => requestUri { uri =>
      log.warning(s"Request to $uri threw a Database Error: ${e.message}")
      complete(ServiceUnavailable, "Database Error")
    }
    case e: LibraryError => requestUri { uri =>
      log.warning(s"Request to $uri threw a Library Error: ${e.message}")
      complete(InternalServerError, "Library Error")
    }
    case RequestError(message) => requestUri { uri =>
      complete(BadRequest, s"Request Error: $message")
    }
    case e: Throwable => requestUri { uri =>
      log.warning(s"Request to $uri threw a an unknown error: ${e.getClass.getName} - ${e.getMessage}")
      complete(InternalServerError)
    }
  }

}
