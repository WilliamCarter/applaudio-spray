package applaudio.services

import java.io.ByteArrayInputStream

import scala.concurrent.Future

trait LibraryService {

  def save(id: Long, inputStream: ByteArrayInputStream): Future[Unit]

}
