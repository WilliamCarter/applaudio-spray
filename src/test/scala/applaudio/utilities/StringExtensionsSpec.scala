package applaudio.utilities

import org.specs2.mutable.Specification

class StringExtensionsSpec extends Specification {
  import StringExtensions._

  "StringExtensions" should {
    "provide a method to trim characters from the start and end of a string" in {
      "\"item\"".trim('"') must be equalTo "item"
    }
    "provide a method to trim strings from the start and end of a string" in {
      "item middle item".trim("item").trim must be equalTo "middle"
    }
    "provide a method to partition a string into a tuple of strings at the first given character" in {
      "one=1=one".partitionAtFirst('=') must be equalTo ("one", "1=one")
    }
  }
}
