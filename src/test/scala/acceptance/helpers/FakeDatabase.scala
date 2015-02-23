package acceptance.helpers

import java.io.File

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.BeforeAfter

import scala.slick.jdbc.JdbcBackend._
import scala.slick.jdbc.StaticQuery

trait FakeDatabase extends BeforeAfter {

  lazy val libraryRoot = new File(ConfigFactory.load().getString("library.root"))

  def executeSQL(query: String) = Database.forConfig("database") withSession { implicit session: Session =>
    try {
      StaticQuery.updateNA(query).execute
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }

  def initialiseDatabase() = executeSQL("CREATE TABLE Track(Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, Title VARCHAR(255) NOT NULL, Artist VARCHAR(255), " +
    "Album VARCHAR(255), AlbumTrack SMALLINT UNSIGNED, Length SMALLINT UNSIGNED, Year SMALLINT UNSIGNED, " +
    "Encoding VARCHAR(32) NOT NULL)")

  def destroyDatabase() = executeSQL("DROP TABLE Track")

  def insertTrack(title: String, artist: String, album: String, albumTrack: Int = 1, length: Int = 210, year: Int = 1999, encoding: String ="mp3") =
    executeSQL(s"INSERT INTO Track (Title, Artist, Album, AlbumTrack, Length, Year, Encoding) " +
    s"VALUES ('$title', '$artist', '$album', $albumTrack, $length, $year, '$encoding')")


  def initialiseLibrary() = libraryRoot.mkdirs()
  def destroyLibrary() = {
    libraryRoot.listFiles.foreach { _.delete() }
    libraryRoot.delete()
  }


  override def before = {
    initialiseLibrary()
    initialiseDatabase()
    bemore
  }

  def bemore = { /* insert data into database if necessary */ }

  override def after = {
    destroyDatabase()
    destroyLibrary()
  }

}
