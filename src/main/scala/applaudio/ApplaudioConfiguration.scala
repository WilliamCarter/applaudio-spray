package applaudio

import com.typesafe.config.ConfigFactory

trait ApplaudioConfiguration {

  val configuration = ConfigFactory.load()

  val libraryRoot = configuration.getString("library.root")

}
