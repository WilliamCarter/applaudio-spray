package applaudio.services

import scala.concurrent.Future

trait LibraryService {

  def save(id: Long, byteArray: Array[Byte]): Future[Unit]

}
