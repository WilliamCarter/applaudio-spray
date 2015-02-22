package applaudio.persistence.library

import java.io.{File, FileInputStream}
import java.nio.file.{Files, Paths}

import applaudio.ApplaudioConfiguration
import applaudio.error.LibraryError
import applaudio.services.LibraryService

import scala.concurrent.Future

class DefaultLibraryService extends LibraryService with ApplaudioConfiguration {

  def save(filename: String, file: File): Future[Unit] = try {
    Future.successful { Files.copy(new FileInputStream(file), Paths.get(s"$libraryRoot/$filename")) }
  } catch {
    case e: Throwable => Future.failed(LibraryError(e.getMessage))
  }

  override def delete(filename: String) = Files.delete(Paths.get(s"$libraryRoot/$filename"))

}
