package com.impassive.recommend.item

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.github.fommil.netlib
import com.impassive.recommend.common.{AlsBoundedPriorityQueue, KafkaDataItem, SimpleLogTools}
import it.unimi.dsi.fastutil.ints.IntOpenHashBigSet
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import scalaj.http.Http

/**
 * Feed 核心推荐逻辑
 */
@SerialVersionUID(1L)
object ItemRecommendSrv extends Serializable {

  val LOGGER_NAME = "com.impassive.recommend.item.ItemRecommendSrv"
  /**
   * 每次给用户推荐的物品数、暂定是 200一次
   */
  private val DEFAULT_NUM = 200;

  private val ONE_HOUR = 3600L * 1000L

  val ONE_DAY = 24L * 3600L * 1000L

  private val CLICKHOUSE_HTTP_URL = ""

  /**
   * CLICK HOUSE 的 HTTP 参数需要 URL 编码
   *
   * @param str 原始字符串
   * @return
   */
  def urlEncode(str: String): String = {
    URLEncoder.encode(str, StandardCharsets.UTF_8.toString)
  }

  def blockify(
                features: RDD[(Int, Array[Double])],
                blockSize: Int = 4096): RDD[Seq[(Int, Array[Double])]] = {
    features.mapPartitions { iter =>
      iter.grouped(blockSize)
    }
  }

  def topByKey(num: Int,
               rdd: RDD[(Int, (Int, Double))],
               ord: Ordering[(Int, Double)]
              ): RDD[(Int, Array[(Int, Double)])] = {
    rdd.aggregateByKey(new AlsBoundedPriorityQueue[(Int, Double)](num)(ord))(
      seqOp = (queue, item) => {
        queue += item
      },
      combOp = (queue1, queue2) => {
        queue1 ++= queue2
      }
    ).mapValues(_.toArray.sorted(ord.reverse))
  }

  /**
   * 使用 rdd 的 join 算法实现 特征矩阵的计算
   */
  def recommendForAll(
                       rank: Int,
                       srcFeatures: RDD[(Int, Array[Double])],
                       dstFeatures: RDD[(Int, Array[Double])],
                       num: Int,
                       spark: SparkSession
                     ): RDD[(Int, Array[(Int, Double)])] = {


    val userCnt = srcFeatures.count().toInt
    SimpleLogTools.info(s"userCnt: ${}, productCnt: ${dstFeatures.count()}", LOGGER_NAME, spark.sparkContext)
    val srcBlocks = blockify(srcFeatures.repartition(userCnt / 4096))
    val dstBlocks = blockify(dstFeatures)
    srcBlocks.persist(StorageLevel.MEMORY_AND_DISK_SER)
    dstBlocks.persist(StorageLevel.MEMORY_AND_DISK_SER)
    val srcBlocksCnt = srcBlocks.count()
    val dstBlocksCnt = dstBlocks.count()

    SimpleLogTools.info(s"userBlockCnt: ${srcBlocksCnt}, productCnt: ${dstBlocksCnt}", LOGGER_NAME, spark.sparkContext)
    if (srcBlocksCnt == 0 || dstBlocksCnt == 0) {
      return spark.sparkContext.emptyRDD
    }

    SimpleLogTools.info(s"start cartesian", LOGGER_NAME, spark.sparkContext)
    val ratings = srcBlocks.cartesian(dstBlocks).flatMap { case (srcIter, dstIter) =>
      val m = srcIter.size
      val n = math.min(dstIter.size, num)
      val output = new Array[(Int, (Int, Double))](m * n)
      var i = 0
      val pq = new AlsBoundedPriorityQueue[(Int, Double)](n)(Ordering.by(_._2))
      srcIter.foreach { case (srcId, srcFactor) =>
        dstIter.foreach { case (dstId, dstFactor) =>
          val score = netlib.BLAS.getInstance().ddot(rank, srcFactor, 1, dstFactor, 1)
          pq += dstId -> score
        }
        pq.foreach { case (dstId, score) =>
          output(i) = (srcId, (dstId, score))
          i += 1
        }
        pq.clear()
      }
      output.toSeq
    }

    topByKey(num, ratings, Ordering.by(_._2))
  }

  /**
   * 推荐并且 广播推荐的结果
   *
   * <ul>
   * <li>20200423 堆糖的日活跃用户大概是 20W - 50W, 每个小时的话不会太多</li>
   * <li>绝对安全白名单池的存在导致 物品的特征矩阵也不会太大、规模也就是 目前的 20W 左右</li>
   * </ul>
   *
   * @param userFeatures    用户的特征矩阵
   * @param productFeatures 物品的特征矩阵
   * @param userIds         要推荐的用户集合
   */
  def recAndPublish(
                     userFeatures: RDD[(Int, Array[Double])],
                     productFeatures: RDD[(Int, Array[Double])],
                     userIds: Broadcast[IntOpenHashBigSet],
                     rank: Int,
                     spark: SparkSession,
                     num: Int = DEFAULT_NUM
                   ): Unit = {
    //    userFeatures.filter(x => userIds.value.contains(x._1))
    val ret = recommendForAll(rank, userFeatures.filter(x => userIds.value.contains(x._1)), productFeatures, num, spark)
    saveToKafka(ret, spark, "topic_feed_rec")
  }

  def saveToKafka(
                   ret: RDD[(Int, Array[(Int, Double)])],
                   spark: SparkSession,
                   topic: String
                 ): Unit = {
    val timeAsSec = System.currentTimeMillis() / 1000L
    import spark.implicits._
    val kafkaDataDF = ret.map(x => {
      val value = x._2.toList.map(recData => {
        recData._1 + "_" + recData._2.formatted("%.4f")
      }).mkString("|")
      KafkaDataItem(null, timeAsSec + ":" + x._1 + ":" + value)
    }).toDF("key", "value")
    kafkaDataDF
      .selectExpr("CAST(value AS STRING)")
      .write.format("kafka")
      .option("kafka.bootstrap.servers", "")
      .option("topic", topic).save()
  }

  /**
   * 查询一段时间内的活跃用户
   *
   * @param hoursBefore 必须 < 24
   */
  def findUsersActiveInHours(hoursBefore: Float): IntOpenHashBigSet = {
    val now = System.currentTimeMillis();
    val endPartition = FastDateFormat.getInstance("yyyyMMdd").format(now).toInt
    val startTimeMills = now - (hoursBefore * ONE_HOUR).toLong
    val startTimeStr = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(startTimeMills)
    val startPartition = FastDateFormat.getInstance("yyyyMMdd").format(startTimeMills).toInt

    val sql = s"SELECT distinct(auth_user_id) FROM dw.t_nginx_www_v2 WHERE toYYYYMMDD(time_iso8601) >= ${startPartition} AND toYYYYMMDD(time_iso8601) <= ${endPartition} AND time_iso8601 > '${startTimeStr}' AND auth_user_id!=0"
    val urlQuery = CLICKHOUSE_HTTP_URL + urlEncode(sql)

    val resp = Http(urlQuery).timeout(0, 0).asString.body
    val userIdStrArray = resp.split("\n")
    val userIds = new IntOpenHashBigSet(userIdStrArray.length)
    for (str <- userIdStrArray) {
      val userId = str.toInt
      if (userId > 0) {
        userIds.add(userId)
      }
    }
    userIds
  }
}
