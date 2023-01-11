package com.impassive.recommend.item

import com.impassive.recommend.common.{KafkaDataItem, SimpleLogTools}
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.sql.SparkSession

/**
 * 定时任务: 每隔一个小时给近1个小时的活跃用户推荐数据 ...
 */
object ItemRecommendHourlyJob {

  val LOGGER_NAME = "com.impassive.recommend.item.ItemRecommendHourlyJob"

  def initConf(): SparkConf = {
    val conf = new SparkConf().setAppName("FeedRecommendHourlyJob")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.set("spark.rdd.compress", "true")
    conf.set("spark.kryoserializer.buffer", "64m")
    conf.set("spark.kryoserializer.buffer.max", "256m")
    conf.registerKryoClasses(Array(classOf[Rating], classOf[Array[Rating]], classOf[KafkaDataItem]))
    conf
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .config(initConf())
      .getOrCreate()
    val sc = spark.sparkContext

    val userIds = ItemRecommendSrv.findUsersActiveInHours(1)

    val userIdsBr = sc.broadcast(userIds)
    /*加载模型*/
    val model = ModelRepo.load(sc, spark)
    ItemRecommendSrv.recAndPublish(
      model._1,
      model._2,
      userIdsBr,
      model._3,
      spark,
      2000
    )

  }


}
