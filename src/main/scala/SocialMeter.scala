/* SimpleApp.scala */
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils

object SocialMeter {
  def main(args: Array[String]) {

    sys.props ++= Map(
      "twitter4j.oauth.consumerKey" -> "e0AxEdyyY6JIqBvEpI7HLnRIH",
      "twitter4j.oauth.consumerSecret" -> "PIOaJHEfBctDSQq6vn5iE2ntJFnGP0kKXAJtNP0CfLYwlX15Vt",
      "twitter4j.oauth.accessToken" -> "2752424182-3O9t4gEYZYC5uD04QEvAN2Y9rg6E1Rz8hFyY0NY",
      "twitter4j.oauth.accessTokenSecret" -> "6bV28Z7y2hPXm8MX3YbJr6PP2XSIn36TRerAhPC9JSUzG"
    )

    val conf = new SparkConf().setMaster("local[2]").setAppName("SocialMeter")
    val ssc = new StreamingContext(conf, Milliseconds(500))

    val tweets = TwitterUtils.createStream(ssc, None, Seq("#bitcoin"))
    val statuses = tweets.map { tweet =>
      println("TWEET: " + tweet.getText)
      tweet.getText
    }

    statuses.print()

    ssc.start()
    Thread.sleep(50000000)
  }
}