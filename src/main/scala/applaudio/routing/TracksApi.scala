package applaudio.routing

import applaudio.models.Track
import applaudio.persistence.library.DefaultLibraryService
import applaudio.services.{LibraryService, TrackService}
import applaudio.persistence.services.SlickTrackService
import spray.http.HttpEntity.NonEmpty
import spray.http._
import spray.httpx.unmarshalling.Unmarshaller
import spray.routing._
import spray.httpx.SprayJsonSupport._
import scala.concurrent.ExecutionContext.Implicits.global
import spray.http.MediaTypes._

import scalaz.{\/, \/-, -\/}


trait TracksApi extends HttpService {

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

            complete{
              libraryService.add(track, fileData).map { disjunction =>
                disjunction.toEither match {
                  case Left(left) => left
                  case Right(unit) => "OK"
                }
              }
            }
          }
        }
      }
    }
  }

}
