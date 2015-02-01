package applaudio.error


abstract class ApplaudioError extends Throwable

case class LibraryError(message: String) extends ApplaudioError
