package applaudio.services

import java.io.ByteArrayInputStream

import scala.concurrent.Future

trait LibraryService {

  def save(filename: String, inputStream: ByteArrayInputStream): Future[Unit]

}
