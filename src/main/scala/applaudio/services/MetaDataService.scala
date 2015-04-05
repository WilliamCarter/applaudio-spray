package applaudio.services

import java.io.File

import applaudio.models.Track
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey

trait MetadataService {
  def metadataForFile(file: File): Track
}

class JAudioTaggerMetadataService extends MetadataService {

  def metadataForFile(mp3: File) = {

    println("metadataForFile")
    println(mp3)

    val tag = AudioFileIO.read(mp3).getTag
    Track(title = tag.getFirst(FieldKey.TITLE),
      artist = Option(tag.getFirst(FieldKey.ARTIST)),
      album = Option(tag.getFirst(FieldKey.ALBUM)),
      albumTrack = Option(tag.getFirst(FieldKey.TRACK)).map(_.toInt),
      year = Option(tag.getFirst(FieldKey.YEAR)).map(_.toInt),
      length = None,
      encoding = "mp3")
  }

}
