package applaudio.persistence.services

import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.slick.driver._
import scala.slick.jdbc.JdbcBackend._
import scala.concurrent.ExecutionContext.Implicits.global


class SlickService extends DatabaseDriver {

  def withSession[T](f: Session => T) = Database.forConfig(DatabaseConfigKey) withSession { implicit session: Session =>
    Future { f(session) }
  }

}

trait DatabaseDriver {

  val DatabaseConfigKey = "database"

  val driver = ConfigFactory.load().getString(s"$DatabaseConfigKey.driver") match {
    case "com.mysql.jdbc.Driver" => MySQLDriver
    case _ => H2Driver
  }

}
