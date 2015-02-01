package applaudio.routing

import akka.actor.Actor
import applaudio.error.LibraryError
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

  val routes: Route = encodeResponse(Gzip) {
    pathPrefix("api") {
      handleExceptions(apiErrorHandler) {
        respondWithMediaType(`application/json`) {
          artistRoutes ~
          albumRoutes ~
          trackRoutes
        }
      }
    } ~
    path("") {
      get {
        respondWithMediaType(`text/html`) {
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

  def apiErrorHandler(implicit log: LoggingContext) = ExceptionHandler {
    case e: LibraryError =>
      requestUri { uri =>
        log.warning(s"Request to $uri threw a Library Error: ${e.message}")
        complete(InternalServerError, e.message)
      }
    case e: Throwable =>
      requestUri { uri =>
        log.warning(s"Request to $uri threw a an unknown error: ${e.getMessage}")
        log.warning(e.getStackTrace.toString)
        complete(InternalServerError)
      }
  }

}
