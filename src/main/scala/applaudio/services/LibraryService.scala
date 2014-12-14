package applaudio.services

import applaudio.models.Track

import scala.concurrent.Future
import scalaz.\/

trait LibraryService {

  def add(track: Track, byteArray: Array[Byte]): Future[\/[String, Unit]]

}
