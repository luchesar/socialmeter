import org.scalatest.WordSpec

class SocialMeterSpec extends WordSpec {
  "Sentiment" should {
    "Analyse the sentiment" in {
      println("Sentiment: " + SocialMeter.sentiment("This movie was actually neither that funny, nor super witty"))
    }
  }
}
