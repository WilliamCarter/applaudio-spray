package applaudio.persistence.library

import java.io.{FileOutputStream, FileInputStream, File}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import applaudio.models.Track
import applaudio.persistence.services.SlickTrackService
import applaudio.services.{TrackService, LibraryService}

import scala.concurrent.Future
import scalaz.\/

class DefaultLibraryService extends LibraryService {

  val trackService: TrackService = new SlickTrackService

  override def add(track: Track, fileData: Array[Byte]): Future[\/[String, Unit]] = {
    println("DefaultLibraryService.add")
    println(track)

    for {
      id <- trackService.add(track)
      save <- saveInLibrary(id, fileData)
    } yield save

  }

  private def saveInLibrary(maybeId: \/[String, Long], byteArray: Array[Byte]): Future[\/[String, Unit]] = {
    Future {
      maybeId.map { id =>

      }
    }
  }

}
