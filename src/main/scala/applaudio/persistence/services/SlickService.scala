package applaudio.persistence.services

import applaudio.ApplaudioConfiguration
import applaudio.error.DatabaseError

import scala.concurrent.Future
import scala.slick.driver._
import scala.slick.jdbc.JdbcBackend._


class SlickService extends DatabaseDriver {

  def withSession[T](f: Session => T) = try {
    Database.forConfig(DatabaseConfigKey) withSession { implicit session: Session => Future.successful { f(session) } }
  } catch {
    case e: Throwable =>
      e.printStackTrace()
      Future.failed(DatabaseError(e.getMessage))
  }

}

trait DatabaseDriver extends ApplaudioConfiguration {

  val DatabaseConfigKey = "database"

  val driver = configuration.getString(s"$DatabaseConfigKey.driver") match {
    case "com.mysql.jdbc.Driver" => MySQLDriver
    case _ => H2Driver
  }

}
