package applaudio.services

import applaudio.error.ApplaudioError

import scala.concurrent.Future
import scalaz.\/

trait LibraryService {

  def save(id: Long, byteArray: Array[Byte]): Future[ApplaudioError\/Long]

}
