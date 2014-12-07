package applaudio.slick.tables

import scala.slick.driver.MySQLDriver.simple._

class TrackTable(tag: Tag) extends Table[(Long, String, Option[String], Option[String], Option[Int], Option[Int], Option[Int], String)](tag, "Track") {

  def id: Column[Long] = column[Long]("Id", O.PrimaryKey, O.AutoInc)

  def title: Column[String] = column[String]("Title", O.NotNull)

  def artist: Column[Option[String]] = column[String]("Artist")

  def album: Column[Option[String]] = column[String]("Album")

  def albumTrack: Column[Option[Int]] = column[Int]("AlbumTrack")

  def length: Column[Option[Int]] = column[Int]("Length")

  def year: Column[Option[Int]] = column[Int]("Year")

  def encoding: Column[String] = column[String]("Encoding")

  def * = (id, title, artist, album, albumTrack, length, year, encoding)
}


//object Track extends TableQuery(new TrackTable(_)) {
//  def byName(name: String) = filter(_.name === name)
//}