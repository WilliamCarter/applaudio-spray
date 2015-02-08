package applaudio.persistence.library

import java.io.ByteArrayInputStream
import java.nio.file.{Files, Paths}

import applaudio.ApplaudioConfiguration
import applaudio.error.LibraryError
import applaudio.services.LibraryService

import scala.concurrent.Future

class DefaultLibraryService extends LibraryService with ApplaudioConfiguration {

  def save(id: Long, inputStream: ByteArrayInputStream): Future[Unit] = try {
    Future.successful { Files.copy(inputStream, Paths.get(s"$libraryRoot/$id")) }
  } catch {
    case e: Throwable => Future.failed(LibraryError(e.getMessage))
  }

}
