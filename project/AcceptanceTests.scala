import sbt._
import Keys._

object AcceptanceTests extends Build {

  val Acceptance = config("acceptance") extend Test

  lazy val root = Project("root", file("."))
    .configs( Acceptance )
    .settings( inConfig(Acceptance)(Defaults.testTasks) : _*)
    .settings( testOptions in Acceptance := Seq( Tests.Filter(_ startsWith "acceptance") ) )
    .settings( parallelExecution in Acceptance := false )

}
