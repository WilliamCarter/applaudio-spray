package applaudio.persistence.services

import applaudio.error.DatabaseError
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.slick.driver._
import scala.slick.jdbc.JdbcBackend._


class SlickService extends DatabaseDriver {

  def withSession[T](f: Session => T) = try {
    Database.forConfig(DatabaseConfigKey) withSession { implicit session: Session => Future.successful { f(session) } }
  } catch {
    case e: Throwable => Future.failed { DatabaseError(e.getMessage) }
  }

}

trait DatabaseDriver {

  val DatabaseConfigKey = "database"

  val driver = ConfigFactory.load().getString(s"$DatabaseConfigKey.driver") match {
    case "com.mysql.jdbc.Driver" => MySQLDriver
    case _ => H2Driver
  }

}
