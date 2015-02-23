
val applicationSettings = Seq(
  name := "Applaudio",
  version := "2.0.0")

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  val specs2Version = "2.4.2"
  Seq(
    "io.spray"            %%  "spray-can"             % sprayV,
    "io.spray"            %%  "spray-routing"         % sprayV,
    "io.spray"            %%  "spray-json"            % "1.3.1",
    "io.spray"            %%  "spray-testkit"         % sprayV            % "test",
    "com.typesafe.akka"   %%  "akka-actor"            % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"          % akkaV             % "test",
    "org.specs2"          %%  "specs2"                % specs2Version     % "test",
    "com.typesafe.slick"  %%  "slick"                 % "2.1.0",
    "mysql"               %   "mysql-connector-java"  % "5.1.23",
    "org.slf4j"           %   "slf4j-nop"             % "1.6.4"
  )
}

testOptions in Test := Seq( Tests.Filter(_ startsWith "applaudio") )

Revolver.settings

applicationSettings
