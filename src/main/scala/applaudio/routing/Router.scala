package applaudio.routing

import akka.actor.Actor
import spray.http.MediaTypes._
import spray.httpx.encoding.Gzip
import spray.routing._


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
