package metrics

import java.util.Locale

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s
import com.danielasfregola.twitter4s.entities.Tweet
import com.redis.RedisClient
import com.twitter.finagle.Thrift
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("twitter-metrics")
    val config = ConfigFactory.load()
    implicit val rc = new RedisClient(config.getString("redis.host"), config.getInt("redis.port"))
    val ts = twitter4s.TwitterStreamingClient()

    ts.sampleStatuses() {
      case tweet: twitter4s.entities.Tweet =>
        val (lang, words) = handleTweet(tweet)
        rc.hincrby("tweets_by_lang", lang, 1)
        words.foreach(word => rc.zincrby(s"words_$lang", word._2, word._1))
    }

    val serviceHost = config.getString("statService.host")
    val servicePort = config.getString("statService.port")
    val statService = Thrift.server.serveIface(s"$serviceHost:$servicePort", new StatService)
  }

  def handleTweet(t: Tweet): (String, Map[String, Int]) = {
    val lang = t.lang
    val locale = lang.map(new Locale(_))
    val text = t.text.toLowerCase(locale.getOrElse(Locale.ENGLISH))
    val words = text
      .split("\\s+")
      .map(_.trim.replaceAll("\\p{P}", ""))
      .filter(_.length > 0)
      .foldLeft(Map.empty[String, Int])((m, x) => m.updated(x, m.getOrElse(x, 0) + 1))

    (lang.getOrElse("other"), words)
  }
}
