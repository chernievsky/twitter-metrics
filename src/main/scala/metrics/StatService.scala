package metrics

import com.redis.RedisClient
import com.twitter.util.{Duration, Future, Return}
import com.redis.serialization.Parse.Implicits._

class StatService (implicit val rc: RedisClient) extends TwitterStatistics.FutureIface {
  override def getTopWords(lang: String, n: Int): Future[Seq[WordStat]] = {
    val top = rc.zrangebyscoreWithScore[String](key = s"words_$lang", limit = Some(0, n), sortAs = RedisClient.DESC)
    val res = top.getOrElse(Seq()).map(x => WordStat(x._1, x._2.toLong))

    Future.value(res)
  }

  override def getNumByLang(lang: String): Future[Long] = {
    val num = rc.hget[Long]("tweets_by_lang", lang)

    Future.value(num.getOrElse(0))
  }
}
