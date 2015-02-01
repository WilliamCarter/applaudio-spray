package applaudio.routing

import applaudio.error.ApplaudioError
import spray.httpx.marshalling.Marshaller

import scalaz.{-\/, \/, \/-}

trait Marshallers {

  implicit def disjunctionMarshaller[T](implicit marshaller: Marshaller[T]): Marshaller[ApplaudioError\/T] = {
    Marshaller[ApplaudioError\/T] { (value, context) =>
      value match {
        case -\/ (error) => context.handleError(error)
        case \/- (response) => marshaller(response, context)
      }
    }
  }

}
