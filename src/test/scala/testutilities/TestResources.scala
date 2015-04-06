package testutilities

import java.io.File

trait TestResources {
  def getResource(filename: String): File =
    new File(getClass.getResource("/" + filename).toURI)
}
