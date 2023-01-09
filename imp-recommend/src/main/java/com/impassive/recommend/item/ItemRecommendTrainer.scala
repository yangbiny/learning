package com.impassive.recommend.item

import com.impassive.recommend.common.{HBaseTool, SimpleLogTools}
import it.unimi.dsi.fastutil.ints.IntOpenHashBigSet
import org.apache.commons.lang3.time.{DateUtils, FastDateFormat}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object ItemRecommendTrainer {

  val LOGGER_NAME = "com.impassive.recommend.item.ItemRecommendTrainer"

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
  private def findWhiteList(sc: SparkContext): IntOpenHashBigSet = {
    @transient val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "rec_feeds_white_list")
    val end = DateUtils.addMonths(new java.util.Date(), -3).getTime
    val feedIdArray = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result]).map(row => {
      val rowKey = row._1.get()
      val feedId = Bytes.toInt(rowKey)
      val creatAt = Bytes.toLong(row._2.getValue(Bytes.toBytes("c"), Bytes.toBytes("gmtCreate")))
      (feedId, creatAt)
    }).filter(x => x._2 >= end).map(x => x._1).collect()
    new IntOpenHashBigSet(feedIdArray)
  }

  def analyzeClickEvents(sc: SparkContext): Unit = {
    @transient val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "")
    val res = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result]).map(row => {
      val rowKey = row._1.get()
      /*0 -11 位是定长 倒转用户Id*/
      val userId = HBaseTool.unnormalize(Bytes.toString(rowKey, 0, 11))
      /*4 个字节表示 feedId 11,12,13,14 */
      val feedId = Bytes.toInt(rowKey, 11, 4)
      /*8 个字节表示 ts , 15,16,17,18,19,20,21,22*/
      val ts = Bytes.toLong(rowKey, 15, 8)
      val salt = Bytes.toInt(rowKey, 23, 4)
      val day = FastDateFormat.getInstance("yyyyMMdd").format(ts)
      (userId, feedId, day, salt)
    })
    res.filter(x => x._3.toInt == 20200722).count()
  }


  def test(sc: SparkContext): Unit = {
    @transient val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "")
    val end = DateUtils.addMonths(new java.util.Date(), -3).getTime
    val feedIdArray = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result]).map(row => {
      val rowKey = row._1.get()
      val feedId = Bytes.toInt(rowKey)
      val creatAt = Bytes.toLong(row._2.getValue(Bytes.toBytes("c"), Bytes.toBytes("gmtCreate")))
      (feedId, creatAt)
    }).map(x => x._1).count()
  }

  private def findClickEvents(sc: SparkContext): RDD[Rating] = {
    @transient val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "")

    sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
      .map(row => {
        val rowKey = row._1.get()
        /*0 -11 位是定长 倒转用户Id*/
        val userId = HBaseTool.unnormalize(Bytes.toString(rowKey, 0, 11))
        /*4 个字节表示 feedId 11,12,13,14 */
        val feedId = Bytes.toInt(rowKey, 11, 4)
        val feedType = Bytes.toString(row._2.getValue(Bytes.toBytes("c"), Bytes.toBytes("type"))).trim
        //        Rating(userId, feedId, 1.0)
        val feedTypeAsInt = if ("atlas".equalsIgnoreCase(feedType.trim)) 1 else 0
        ClickEvent(userId, feedId, feedTypeAsInt)
      }).filter(_.feedType == 1).map(x => Rating(x.userId, x.feedId, 1.0))
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .config(initConf())
      .getOrCreate()
    val sc = spark.sparkContext
    SimpleLogTools.info("start Training", LOGGER_NAME, sc)


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

    /*feed 必须在白名单中 */
    val whiteFeedList = findWhiteList(sc);
    SimpleLogTools.info(s"found White List Feed ${whiteFeedList.size64()}", LOGGER_NAME, sc)
    val validFeeds = sc.broadcast(whiteFeedList)
    val productFeatures = model.productFeatures.filter(x => {
      validFeeds.value.contains(x._1)
    })
    val filterFeedCnt = productFeatures.count()

    SimpleLogTools.info(s"After filter white list, remain feed cnt: ${filterFeedCnt}", LOGGER_NAME, sc)
    /*先保存模型*/
    ModelRepo.save(sc, spark, userFeatures, productFeatures, rank)
    SimpleLogTools.info("Save Model SUCCESS", LOGGER_NAME, sc)

    /*给最近1周内的活跃用户推荐*/
    SimpleLogTools.info("start find Users", LOGGER_NAME, sc)
    val userIds = ItemRecommendSrv.findUsersActiveInHours(24 * 7)
    SimpleLogTools.info(s"Finish Find Users, cnt: ${userIds.size64()}", LOGGER_NAME, sc)
    val userIdsBr = sc.broadcast(userIds)

    ItemRecommendSrv.recAndPublish(
      userFeatures,
      productFeatures,
      userIdsBr,
      rank,
      spark
    )
    SimpleLogTools.info("finish Training", LOGGER_NAME, sc)
  }
}
