package test

import akka.actor._
import com.danielasfregola.twitter4s
import com.twitter.conversions.time._
import com.twitter.finagle.Thrift

// Notice that these Future's are a little bit different from Scala Futures.
import com.twitter.util.{Future, Return, Duration}

object Example {
  def main(args: Array[String]): Unit = {
    // Akka initialization
    implicit val system = ActorSystem("twitter-example")

    // Twitter client initialization
    val twitter = twitter4s.TwitterStreamingClient()

    // draws sample stream of twitter events, takes handler as second argument
    twitter.sampleStatuses() {
      case tweet: twitter4s.entities.Tweet => println(tweet.text)
    }

    // instantiate thrift server on given address
    val service = Thrift.server.serveIface("127.0.0.1:12345", new TwitterStatisticsImpl)

    // the app will not stop here, because akka's actor system spawns a daemon thread,
    // and the app will wait its termination (no way in this example)
  }
}

// implementation of twitter statistics interface (generate from thrift)
class TwitterStatisticsImpl extends TwitterStatistics.FutureIface {
  override def getTweet(id: Long): Future[Tweet] = {
    Future.const(Return(test.Tweet(id, "Hello, world!!!", "en")))
  }
}