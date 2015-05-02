import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import spray.revolver.RevolverPlugin.Revolver
import sbt.complete.DefaultParsers._

object ApplaudioBuild extends Build {

  val ApplaudioVersion = "2.0.0"

  val ScalaVersion = "2.11.2"
  val AkkaVersion = "2.3.6"
  val SprayVersion = "1.3.2"
  val SprayJsonVersion = "1.3.1"
  val Specs2Version = "2.4.2"

  lazy val applaudioDependencies = Seq (
    "io.spray"            %%  "spray-can"             % SprayVersion,
    "io.spray"            %%  "spray-routing"         % SprayVersion,
    "io.spray"            %%  "spray-json"            % SprayJsonVersion,
    "com.typesafe.akka"   %%  "akka-actor"            % AkkaVersion,
    "com.typesafe.slick"  %%  "slick"                 % "2.1.0",
    "mysql"               %   "mysql-connector-java"  % "5.1.23",
    "org.slf4j"           %   "slf4j-nop"             % "1.6.4",
    "org"                 %   "jaudiotagger"          % "2.0.3",
    "io.spray"            %%  "spray-testkit"         % SprayVersion      % "test",
    "com.typesafe.akka"   %%  "akka-testkit"          % AkkaVersion       % "test",
    "org.specs2"          %%  "specs2"                % Specs2Version     % "test",
    "com.h2database"      %   "h2"                    % "1.4.185"         % "test" )

  lazy val applaudioSettings = Seq(
    version       := ApplaudioVersion,
    scalaVersion  := ScalaVersion,
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    resolvers     += "spray" at "http://repo.spray.io/",
    libraryDependencies ++= applaudioDependencies,
    fullClasspath in Revolver.reStart <<= (fullClasspath in Revolver.reStart, baseDirectory) map { (classpath, base) =>
      Attributed.blank(base / "src" / "frontend") +: classpath
    },
    testOptions in Test := Seq( Tests.Filter(_ startsWith "applaudio") ),
    shellPrompt := { state: State => "[applaudio] > " } )

  lazy val Acceptance = config("acceptance") extend Test
  lazy val acceptanceSettings = inConfig(Acceptance)(Defaults.testTasks) ++ Seq(
    testOptions in Acceptance := Seq( Tests.Filter(_ startsWith "acceptance") ),
    parallelExecution in Acceptance := false )

  lazy val grunt = inputKey[Unit]("Run grunt tasks")
  lazy val gruntSettings = Seq(
    grunt :=  {
      val gruntTaskId = StringBasic.parsed.stripPrefix(":")
      println(s"grunt $gruntTaskId")
      Process(s"grunt $gruntTaskId", file("src/frontend")).!
    } )

  lazy val deploymentSettings = Seq(
    assemblyJarName in assembly := s"Applaudio-$ApplaudioVersion.jar",
    test in assembly := {},
    assembly <<= assembly.dependsOn(grunt.toTask("deploy")) )

  lazy val Applaudio = Project(
    id = "Applaudio",
    base = file("."),
    configurations = Seq(Acceptance),
    settings = Revolver.settings ++ applaudioSettings ++ acceptanceSettings ++ gruntSettings ++ deploymentSettings )

}
