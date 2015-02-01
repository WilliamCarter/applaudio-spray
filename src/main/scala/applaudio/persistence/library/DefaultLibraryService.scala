package applaudio.persistence.library

import java.io.FileOutputStream

import applaudio.ApplaudioConfiguration
import applaudio.persistence.services.SlickTrackService
import applaudio.services.{LibraryService, TrackService}
import applaudio.error.{LibraryError, ApplaudioError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.{-\/, \/, \/-}

class DefaultLibraryService extends LibraryService with ApplaudioConfiguration {

  val trackService: TrackService = new SlickTrackService

  def save(id: Long, byteArray: Array[Byte]): Future[ApplaudioError\/Long] = Future {
    try {
      val fos = new FileOutputStream(s"$libraryRoot/$id")
      fos.write(byteArray)
      fos.close() // Neater way to do this?
      \/-(id)
    } catch {
      case e: Exception => -\/ (LibraryError(e.getMessage))
    }
  }

}
