package applaudio.services

import applaudio.models.Track
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import testutilities.TestResources

class JAudioTaggerMetadataServiceSpec extends Specification {

  "The JAudioTagger Metadata Service" can {
    "extract the track length from an audio file" in new MetadataServiceScope {
      jAudioTaggerMetadataService.trackLength(coverMe) must be equalTo expectedMetadata.length.get
    }

    "extract all metadata from an audio file" in new MetadataServiceScope {
      jAudioTaggerMetadataService.allMetadata(coverMe) must be equalTo expectedMetadata
    }
  }
}

trait MetadataServiceScope extends Scope with TestResources {
  val jAudioTaggerMetadataService = new JAudioTaggerMetadataService
  val coverMe = getResource("Cover Me.mp3")
  val expectedMetadata = Track("Cover Me", Some("Björk"), Some("Post"), Some(10), Some(126), Some(1995), "mp3")
}