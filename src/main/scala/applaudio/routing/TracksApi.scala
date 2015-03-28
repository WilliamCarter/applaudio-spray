package applaudio.routing

import java.io.File

import applaudio.error.RequestError
import applaudio.models.Track
import applaudio.services.{LibraryService, TrackService}
import applaudio.utilities.FileUpload
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait TracksApi extends HttpService with FileUpload {

  def trackService: TrackService
  def libraryService: LibraryService

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
          withTrackUpload { case (track, file) =>
            // TODO: check encoding type at some point
            complete {
              upload(track, file)
            }
          }
        }
      }
    }
  }

  def upload(track: Track, file: File): Future[Track] = for {
    id <- trackService.add(track)
    saved <- libraryService.save(s"$id.${track.encoding}", file)
  } yield track.copy(id = Some(id))


  def withTrackUpload: Directive1[(Track, File)] = withMultipartFormData.flatMap { multipartFormData =>

    (for {
      title <- multipartFormData.fields.get("title")
      encoding <- multipartFormData.fields.get("encoding")
      file <- multipartFormData.file
    } yield {

      val track = Track(
        title = title,
        artist = multipartFormData.fields.get("artist"),
        album = multipartFormData.fields.get("album"),
        albumTrack =  multipartFormData.fields.get("albumTrack").map(_.toInt),
        length = multipartFormData.fields.get("length").map(_.toInt),
        year = multipartFormData.fields.get("length").map(_.toInt),
        encoding = encoding)

      provide(track, file.data)
    }).getOrElse(failWith(RequestError("Unable to create track data from given request")))
  }

}
