package applaudio.services

import java.io.File

import scala.concurrent.Future

trait LibraryService {
  def get(filename: String): File
  def save(filename: String, file: File): Future[Unit]
  def delete(filename: String)
}
