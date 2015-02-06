package applaudio.error


abstract class ApplaudioError extends Throwable

case class LibraryError(message: String) extends ApplaudioError
case class DatabaseError(message: String) extends ApplaudioError
