/* SimpleApp.scala */

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.util.CoreMap
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils
import scala.collection.JavaConversions._

object SocialMeter {
  def main(args: Array[String]) {

    sys.props ++= Map(
      "twitter4j.oauth.consumerKey" -> "e0AxEdyyY6JIqBvEpI7HLnRIH",
      "twitter4j.oauth.consumerSecret" -> "PIOaJHEfBctDSQq6vn5iE2ntJFnGP0kKXAJtNP0CfLYwlX15Vt",
      "twitter4j.oauth.accessToken" -> "2752424182-3O9t4gEYZYC5uD04QEvAN2Y9rg6E1Rz8hFyY0NY",
      "twitter4j.oauth.accessTokenSecret" -> "6bV28Z7y2hPXm8MX3YbJr6PP2XSIn36TRerAhPC9JSUzG"
    )

    val conf = new SparkConf().setMaster("local[2]").setAppName("SocialMeter")
    val ssc = new StreamingContext(conf, Milliseconds(200))

    val tweets = TwitterUtils.createStream(ssc, None, args.toSeq)
    val statuses = tweets.map { tweet =>
      val sent = sentiment(tweet.getText)
      println(s"TWEET: ${color(sent)} $sent ${tweet.getText} ${Console.RESET}")
      tweet.getText
    }

    statuses.print()

    ssc.start()
    Thread.sleep(500000000)
  }

  private def color(sentiment: Double) = Math.round(sentiment) match {
    case 0 => Console.RED
    case 1 => Console.MAGENTA
    case 2 => Console.YELLOW
    case 3 => Console.CYAN
    case 4 => Console.GREEN
    case _ => Console.RED_B + Console.BLINK
  }

  def sentiment(tweet: String): Double = {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")
    val pipeline = new StanfordCoreNLP(props)
    val annotation = pipeline.process(tweet)

    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    sentences.foldLeft(0D) {
      case (sentiment, sentence) =>
        val tree = sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
        val sentenceSentiment = RNNCoreAnnotations.getPredictedClass(tree)
        sentiment + sentenceSentiment
    }/sentences.length
  }
}