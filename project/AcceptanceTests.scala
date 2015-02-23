import sbt._
import Keys._

object AcceptanceTests extends Build {

  lazy val Acceptance = config("acceptance") extend Test

  lazy val root = Project("root", file("."))
    .configs( Acceptance )
    .settings( inConfig(Acceptance)(Defaults.testTasks) : _*)
    .settings( testOptions in Acceptance := Seq( Tests.Filter(_ startsWith "acceptance") ) )
    .settings( parallelExecution in Acceptance := false )
    .settings( libraryDependencies += "com.h2database" % "h2" % "1.4.185" % "test") // Using "acceptance flag adds to libraryDependencies but not to classpath!

}
