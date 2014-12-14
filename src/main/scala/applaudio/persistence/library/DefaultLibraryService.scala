package applaudio.persistence.library

import java.io.FileOutputStream

import applaudio.ApplaudioConfiguration
import applaudio.models.Track
import applaudio.persistence.services.SlickTrackService
import applaudio.services.{LibraryService, TrackService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.{-\/, \/-, \/}

class DefaultLibraryService extends LibraryService with ApplaudioConfiguration {

  val trackService: TrackService = new SlickTrackService

  override def add(track: Track, fileData: Array[Byte]): Future[String \/ Unit] = for {
    idOrError <- trackService.add(track)
    resultOrError <- saveInLibrary(idOrError, fileData)
  } yield resultOrError


  private def saveInLibrary(maybeId: String \/ Long, byteArray: Array[Byte]): Future[String \/ Unit] = Future {
    maybeId.flatMap { id =>
      try \/- {
        val fos = new FileOutputStream(s"$libraryRoot$id")
        fos.write(byteArray)
        fos.close() // Neater way to do this?
      } catch {
        case e: Exception => {
//            trackService.delete(id) // TODO: do this
          -\/(e.getMessage)
        }
      }
    }
  }

}
