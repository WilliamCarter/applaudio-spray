package applaudio.services

import java.io.File
import java.util.logging.{Logger, Level}

import applaudio.models.Track
import org.jaudiotagger.audio.{AudioFile, AudioFileIO}
import org.jaudiotagger.tag.FieldKey

trait MetadataService {
  def allMetadata(file: File): Track
  def trackLength(file: File): Int
}

class JAudioTaggerMetadataService extends MetadataService {

  Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF)

  def allMetadata(mp3: File): Track = {
    val audioFile = AudioFileIO.read(mp3)
    val tag = audioFile.getTag
    Track(title = Option(tag.getFirst(FieldKey.TITLE)).getOrElse(""),
      artist = Option(tag.getFirst(FieldKey.ARTIST)),
      album = Option(tag.getFirst(FieldKey.ALBUM)),
      albumTrack = Option(tag.getFirst(FieldKey.TRACK)).map(_.toInt),
      year = Option(tag.getFirst(FieldKey.YEAR)).map(_.toInt),
      length = Some(trackLength(audioFile)),
      encoding = "mp3")
  }

  def trackLength(file: File): Int = trackLength(AudioFileIO.read(file))
  def trackLength(audioFile: AudioFile): Int = audioFile.getAudioHeader.getTrackLength

}
