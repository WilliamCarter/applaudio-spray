package acceptance.helpers

import org.specs2.mutable.BeforeAfter

import scala.slick.jdbc.JdbcBackend._
import scala.slick.jdbc.StaticQuery

trait FakeDatabase extends BeforeAfter {

  def executeSQL(query: String) = Database.forConfig("database") withSession { implicit session: Session =>
    try {
      StaticQuery.updateNA(query).execute
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }

  override def before = executeSQL("CREATE TABLE Track(Id INT PRIMARY KEY, Title VARCHAR(255) NOT NULL, Artist VARCHAR(255), " +
    "Album VARCHAR(255), AlbumTrack SMALLINT UNSIGNED, Length SMALLINT UNSIGNED, Year SMALLINT UNSIGNED, " +
    "Encoding VARCHAR(32) NOT NULL)")

  override def after = executeSQL("DROP TABLE Track")

}
