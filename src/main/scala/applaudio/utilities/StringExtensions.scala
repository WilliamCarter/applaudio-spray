package applaudio.utilities

object StringExtensions {
  implicit class ExtendedString(string: String) {
    def trim(s: String): String = string.trim.stripPrefix(s).stripSuffix(s)
    def trim(c: Char): String = trim(c.toString)

    def partitionAtFirst(c: Char): (String, String) = (string.takeWhile(_ != c), string.dropWhile(_ != c).tail)
  }
}
