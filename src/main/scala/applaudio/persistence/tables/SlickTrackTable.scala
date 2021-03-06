package applaudio.persistence.tables

import applaudio.models.Track
import applaudio.persistence.services.DatabaseDriver

trait SlickTrackTable { this: DatabaseDriver =>

  import driver.simple._

  class TrackTable(tag: Tag) extends Table[(Option[Long], String, Option[String], Option[String], Option[Int], Option[Int], Option[Int], String)](tag, "Track") {

    def id: Column[Long] = column[Long]("Id", O.PrimaryKey, O.AutoInc)

    def title: Column[String] = column[String]("Title", O.NotNull)

    def artist: Column[Option[String]] = column[String]("Artist")

    def album: Column[Option[String]] = column[String]("Album")

    def albumTrack: Column[Option[Int]] = column[Int]("AlbumTrack")

    def length: Column[Option[Int]] = column[Int]("Length")

    def year: Column[Option[Int]] = column[Int]("Year")

    def encoding: Column[String] = column[String]("Encoding")

    def * = (id.?, title, artist, album, albumTrack, length, year, encoding)
  }


  object Tracks extends TableQuery(new TrackTable(_)) {

    def byArtist(artist: String) = filter(_.artist === artist)

    def byAlbum(artist: String, album: String) = byArtist(artist).filter(_.album === album)

  }

  implicit def fromRow(row: (Option[Long], String, Option[String], Option[String], Option[Int], Option[Int], Option[Int], String)): Track = {
    Track(row._1, row._2, row._3, row._4, row._5, row._6, row._7, row._8)
  }


}