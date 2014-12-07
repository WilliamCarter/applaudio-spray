package applaudio.routing

import akka.actor.Actor
import applaudio.fakeservices.FakeArtistsService
import applaudio.services.ArtistsService
import applaudio.slick.SlickArtistsService
import spray.httpx.encoding.Gzip
import spray.routing._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._



class Router extends Actor with ApplaudioRouting {
  def actorRefFactory = context
  override def receive: Receive = runRoute(routes)
}

trait ApplaudioRouting extends HttpService {

  val artistsService: ArtistsService = new SlickArtistsService

  val routes: Route = encodeResponse(Gzip) {
    pathPrefix("api") {
      path("artists") {
        get {
          respondWithMediaType(`application/json`) {
            complete {
              artistsService.all
            }
          }
        }
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