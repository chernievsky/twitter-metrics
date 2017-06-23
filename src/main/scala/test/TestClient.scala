package test

import com.twitter.finagle.Thrift
import metrics.TwitterStatistics
import com.twitter.util.{Await, Duration, Future, Return}
import com.typesafe.config.ConfigFactory

object TestClient extends App {
  val config = ConfigFactory.load()
  val serviceHost = config.getString("statService.host")
  val servicePort = config.getString("statService.port")

  val client = Thrift.client.newIface[TwitterStatistics[Future]](s"$serviceHost:$servicePort"
  )
  val enTweets = client.getNumByLang("en")
  val topEnWords = client.getTopWords("en", 10)

  println(Await.result(enTweets, Duration.Top))
  println(Await.result(topEnWords, Duration.Top))
}
