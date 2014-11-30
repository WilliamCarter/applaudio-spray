package applaudio.models

import org.specs2.mutable.Specification
import spray.json._

class MyServiceSpec extends Specification {

  val bobDylanObject = Artist("Bob Dylan")
  val bobDylanJson =
    """
      |{ "name": "Bob Dylan" }
    """.stripMargin.parseJson

  "The Artist Object" can {
    "serialise to JSON" in {
      bobDylanObject.toJson should beEqualTo (bobDylanJson)
    }
  }

}
