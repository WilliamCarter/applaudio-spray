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

            libraryService.add(track, fileData)
            complete("OK")
          }
//          entity(as[MultipartFormData]) { data =>
//
////            println(data.get("artist"))
////            println(data.get("file"))
//
//            complete("hmm")
//          }
        }
      }
    }
  }

}

//case class TrackUpload(track: Track, fileData: Array[Byte])
//object TrackUpload {
//  implicit def unmarshaller = Unmarshaller[TrackUpload](`application/x-www-form-urlencoded`) {
//    case HttpEntity.NonEmpty(contentType, data) =>
//      val formData = data.as[MultipartFormData]
//      TrackUpload(
//        Track(),
//        data.get("file").get
//      )
//  }
//}