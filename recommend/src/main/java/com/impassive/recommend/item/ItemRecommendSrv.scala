package com.impassive.recommend.item

import com.github.fommil.netlib
import com.impassive.recommend.common.{AlsBoundedPriorityQueue, KafkaDataItem}
import it.unimi.dsi.fastutil.ints.IntOpenHashBigSet
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

/**
 * Feed 核心推荐逻辑
 */
@SerialVersionUID(1L)
object ItemRecommendSrv extends Serializable {

  /**
   * 每次给用户推荐的物品数、暂定是 200一次
   */
  private val DEFAULT_NUM = 200

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
  private def recommendForAll(
                               rank: Int,
                               srcFeatures: RDD[(Int, Array[Double])],
                               dstFeatures: RDD[(Int, Array[Double])],
                               num: Int,
                               spark: SparkSession
                             ): RDD[(Int, Array[(Int, Double)])] = {


    val userCnt = srcFeatures.count().toInt

    val srcBlocks = blockify(srcFeatures.repartition(userCnt / 4096))
    val dstBlocks = blockify(dstFeatures)

    srcBlocks.persist(StorageLevel.MEMORY_AND_DISK_SER)
    dstBlocks.persist(StorageLevel.MEMORY_AND_DISK_SER)
    val srcBlocksCnt = srcBlocks.count()
    val dstBlocksCnt = dstBlocks.count()

    if (srcBlocksCnt == 0 || dstBlocksCnt == 0) {
      return spark.sparkContext.emptyRDD
    }

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
    val ret = recommendForAll(rank, userFeatures.filter(x => userIds.value.contains(x._1)), productFeatures, num, spark)
    saveToKafka(ret, spark, "kafka topic")
  }

  private def saveToKafka(
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
      .selectExpr("CAST(value AS STRING)") // 将 Kafka data item 里面的 value 转换为 string
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
    new IntOpenHashBigSet()
  }
}
