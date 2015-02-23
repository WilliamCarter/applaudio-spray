package acceptance.helpers

import akka.actor.ActorRefFactory
import applaudio.routing.ApplaudioRouting
import org.specs2.mutable.Specification
import spray.httpx.ResponseTransformation
import spray.httpx.encoding.Gzip
import spray.testkit.Specs2RouteTest

trait ApplaudioAcceptance extends Specification with Specs2RouteTest with ApplaudioRouting with ResponseTransformation {
  def actorRefFactory: ActorRefFactory = system
  sequential

  val unzippedRoutes = mapHttpResponse(decode(Gzip))(routes)
}
