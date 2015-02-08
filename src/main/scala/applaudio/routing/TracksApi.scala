package applaudio.routing

import java.io.ByteArrayInputStream

import applaudio.models.Track
import applaudio.persistence.library.DefaultLibraryService
import applaudio.persistence.services.SlickTrackService
import applaudio.services.{LibraryService, TrackService}
import spray.http.BodyPart
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


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
              'file.as[BodyPart]) { (title, artist, album, albumTrack, length, year, encoding, file) =>

            val track = Track(title, artist, album, albumTrack, length, year, encoding)

            complete {
              upload(track, new ByteArrayInputStream(file.entity.data.toByteArray))
            }
          }
        }
      }
    }
  }

  def upload(track: Track, data: ByteArrayInputStream): Future[Track] = for {
    id <- trackService.add(track)
    saved <- libraryService.save(s"$id.${track.encoding}", data)
  } yield track.copy(id = Some(id))

}
