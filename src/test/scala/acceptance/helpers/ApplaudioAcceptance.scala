package acceptance.helpers

import akka.actor.ActorRefFactory
import applaudio.routing.ApplaudioRouting
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

trait ApplaudioAcceptance extends Specification with Specs2RouteTest with ApplaudioRouting {
  def actorRefFactory: ActorRefFactory = system
  sequential
}
