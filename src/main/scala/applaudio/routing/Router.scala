package applaudio.routing

import akka.actor.Actor
import applaudio.fakeservices.FakeArtistsService
import applaudio.services.{TrackService, ArtistService}
import applaudio.slick.services.{SlickTrackService, SlickArtistService}
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

  val artistService: ArtistService = new SlickArtistService
  val trackService: TrackService = new SlickTrackService

  val routes: Route = encodeResponse(Gzip) {
    pathPrefix("api") {
      respondWithMediaType(`application/json`) {
        path("artists") {
          get {
            complete {
              artistService.all
            }
          }
        } ~
        path("tracks" / Segment) { artist =>
          get {
            complete {
              trackService.byArtist(artist)
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