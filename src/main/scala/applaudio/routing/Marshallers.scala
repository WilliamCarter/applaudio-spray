package applaudio.routing

//trait Marshallers {
//
//  implicit def disjunctionMarshaller[T](implicit marshaller: Marshaller[T]): Marshaller[ApplaudioError\/T] = {
//    Marshaller[ApplaudioError\/T] { (value, context) =>
//      value match {
//        case -\/ (error) => context.handleError(error)
//        case \/- (response) => marshaller(response, context)
//      }
//    }
//  }
//
//}
