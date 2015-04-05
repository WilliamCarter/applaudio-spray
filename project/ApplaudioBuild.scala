import sbt._
import sbt.Keys._
import spray.revolver.RevolverPlugin.Revolver

object ApplaudioBuild extends Build {

  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  val specs2Version = "2.4.2"

  lazy val applaudioDependencies = Seq (
    "io.spray"            %%  "spray-can"             % sprayV,
    "io.spray"            %%  "spray-routing"         % sprayV,
    "io.spray"            %%  "spray-json"            % "1.3.1",
    "com.typesafe.akka"   %%  "akka-actor"            % akkaV,
    "com.typesafe.slick"  %%  "slick"                 % "2.1.0",
    "mysql"               %   "mysql-connector-java"  % "5.1.23",
    "org.slf4j"           %   "slf4j-nop"             % "1.6.4",
    "org"                 %   "jaudiotagger"          % "2.0.3",
    "io.spray"            %%  "spray-testkit"         % sprayV            % "test",
    "com.typesafe.akka"   %%  "akka-testkit"          % akkaV             % "test",
    "org.specs2"          %%  "specs2"                % specs2Version     % "test",
    "com.h2database"      %   "h2"                    % "1.4.185"         % "test" )

  lazy val applaudioSettings = Seq (
    version       := "2.0.0",
    scalaVersion  := "2.11.2",
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    libraryDependencies ++= applaudioDependencies,
    fullClasspath in Revolver.reStart <<= (fullClasspath in Revolver.reStart, baseDirectory) map { (classpath, base) =>
      Attributed.blank(base / "src" / "frontend") +: classpath
    },
    testOptions in Test := Seq( Tests.Filter(_ startsWith "applaudio") ),
    shellPrompt := { state: State => "[applaudio] > " }
  )

  lazy val Acceptance = config("acceptance") extend Test
  lazy val acceptanceSettings = inConfig(Acceptance)(Defaults.testTasks) ++ Seq (
    testOptions in Acceptance := Seq( Tests.Filter(_ startsWith "acceptance") ),
    parallelExecution in Acceptance := false )

  lazy val Applaudio = Project(
    id = "Applaudio",
    base = file("."),
    configurations = Seq(Acceptance),
    settings = Revolver.settings ++ applaudioSettings ++ acceptanceSettings)

}
