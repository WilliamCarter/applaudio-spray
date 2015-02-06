package applaudio.persistence.library

import java.nio.file.{Files, Paths}

import applaudio.ApplaudioConfiguration
import applaudio.error.LibraryError
import applaudio.services.LibraryService

import scala.concurrent.Future

class DefaultLibraryService extends LibraryService with ApplaudioConfiguration {

  def save(id: Long, byteArray: Array[Byte]): Future[Unit] = try {
    Future.successful { Files.write(Paths.get(s"$libraryRoot/$id"), byteArray) }
  } catch {
    case e: Throwable => Future.failed(LibraryError(e.getMessage))
  }

}
