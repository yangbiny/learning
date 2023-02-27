package com.impassive.recommend.item

import it.unimi.dsi.fastutil.ints.IntOpenHashBigSet
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object ItemRecommendTrainer {


  def initConf(): SparkConf = {
    val conf = new SparkConf().setAppName("FeedRecommender")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.set("spark.rdd.compress", "true")
    conf.set("spark.kryoserializer.buffer", "64m")
    conf.set("spark.kryoserializer.buffer.max", "256m")
    conf.registerKryoClasses(Array(classOf[WhiteListFeedItem], classOf[ClickEvent], classOf[Rating], classOf[Array[Rating]]))
    conf
  }

  case class ClickEvent(
                         userId: Int,
                         feedId: Int,
                         feedType: Int
                       )

  case class WhiteListFeedItem(
                                feedId: Int,
                                createAt: Long
                              )


  /**
   * <p>
   * 业务上规定了严格的 Feed 白名单系统,
   * 只有白名单系统中的  Feed 才允许被推荐
   * </p>
   *
   * @param sc
   */
  private def findSafeProductList(sc: SparkContext): IntOpenHashBigSet = {
    // 需要过滤的数据
    new IntOpenHashBigSet()
  }

  private def findClickEvents(sc: SparkContext): RDD[Rating] = {
    new
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .config(initConf())
      .getOrCreate()
    val sc = spark.sparkContext


    val rank = 20
    /*最大迭代次数为10次*/
    val iterations = 10
    val lambda = 0.01
    val alpha = 0.01
    /*默认是 -1*/
    val blockSize = -1
    val clickEvents = findClickEvents(sc)
    val model = ALS.trainImplicit(clickEvents, rank, iterations, lambda, blockSize, alpha)
    val userFeatures = model.userFeatures

    val safe = findSafeProductList(sc);

    val validFeeds = sc.broadcast(safe)
    val productFeatures = model.productFeatures.filter(x => {
      validFeeds.value.contains(x._1)
    })

    /*先保存模型*/
    ModelRepo.save(sc, spark, userFeatures, productFeatures, rank)

    /*给最近1周内的活跃用户推荐*/
    val userIds = ItemRecommendSrv.findUsersActiveInHours(24 * 7)
    val userIdsBr = sc.broadcast(userIds)

    ItemRecommendSrv.recAndPublish(
      userFeatures,
      productFeatures,
      userIdsBr,
      rank,
      spark
    )
  }
}
