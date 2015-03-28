package applaudio.persistence.library

import java.io.{File, FileInputStream}
import java.nio.file.{Files, Paths}

import applaudio.ApplaudioConfiguration
import applaudio.error.LibraryError
import applaudio.services.LibraryService

import scala.concurrent.Future

class DefaultLibraryService extends LibraryService with ApplaudioConfiguration {

  override def get(filename: String) = new File(path(filename))

  def save(filename: String, file: File): Future[Unit] = try {
    Future.successful { Files.copy(new FileInputStream(file), Paths.get(path(filename))) }
  } catch {
    case e: Throwable => Future.failed(LibraryError(e.getMessage))
  }

  override def delete(filename: String) = Files.delete(Paths.get(path(filename)))

  private def path(filename: String) = s"$libraryRoot/$filename"
}
