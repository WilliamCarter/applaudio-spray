package applaudio.routing

import applaudio.error.ApplaudioError
import applaudio.models.Track
import applaudio.persistence.library.DefaultLibraryService
import applaudio.persistence.services.SlickTrackService
import applaudio.services.{LibraryService, TrackService}
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.\/


trait TracksApi extends HttpService with Marshallers {

  val trackService: TrackService = new SlickTrackService
  val libraryService: LibraryService = new DefaultLibraryService

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
          formFields(
              'title.as[String],
              'artist.as[Option[String]],
              'album.as[Option[String]],
              'albumTrack.as[Option[Int]],
              'length.as[Option[Int]],
              'year.as[Option[Int]],
              'encoding.as[String],
              'file.as[Array[Byte]]) { (title, artist, album, albumTrack, length, year, encoding, fileData) =>

            val track = Track(title, artist, album, albumTrack, length, year, encoding)

            complete {
              upload(track, fileData)
            }
          }
        }
      }
    }
  }

  def upload(track: Track, data: Array[Byte]): Future[ApplaudioError\/Track] = for {
    id <- trackService.add(track)
    saved <- libraryService.save(id, data)
  } yield {
    saved.map{ id => track.copy(id = Some(id)) }
  }

}
