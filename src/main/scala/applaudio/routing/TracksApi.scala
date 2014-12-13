package applaudio.routing

import applaudio.services.TrackService
import applaudio.slick.services.SlickTrackService
import spray.http.MultipartFormData
import spray.routing._
import spray.httpx.SprayJsonSupport._
import scala.concurrent.ExecutionContext.Implicits.global

trait TracksApi extends HttpService {

  val trackService: TrackService = new SlickTrackService

  val trackRoutes: Route = {
    pathPrefix("tracks") {
      path(Segment) { artist =>
        get {
          complete {
            trackService.byArtist(artist)
          }
        }
      } ~
      path(Segment / Segment) { (artist, album) =>
        get {
          complete {
            trackService.byAlbum(artist, album)
          }
        }
      } ~
      path("upload") {
        post {

          entity(as[MultipartFormData]) { data =>
            println(data)
            complete("OK")
          }
        }
      }
    }
  }

}
