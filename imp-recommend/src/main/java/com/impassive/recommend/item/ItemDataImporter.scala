package com.impassive.recommend.item

import com.google.gson.Gson
import com.impassive.recommend.common.HBaseTool
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkContext

object ItemDataImporter {

  case class FeedClickData(user_id: Int, timestamp: Int, subkey: String, feedid: Int, feed_id: Int, feed_type: String)

  def rowKey(feedClickData: FeedClickData): Array[Byte] = {
    val b1 = Bytes.toBytes(HBaseTool.normalize(feedClickData.user_id.toString, 11))
    val b2 = Bytes.toBytes(feedClickData.feed_id)
    val b3 = Bytes.toBytes(feedClickData.timestamp)
    Bytes.add(Array(b1, b2, b3))
  }

  def readSingleFile(sc: SparkContext, fileName: String): Unit = {
    //    sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result]).count()
    @transient val hbaseConf = HBaseConfiguration.create()
    @transient val jobConf = new JobConf(hbaseConf)
    jobConf.set("hbase.zookeeper.quorum", "")
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "feed_click_event")
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    sc.textFile(fileName).map { x =>
      var data = FeedClickData(0, 0, "", 0, 0, "")
      try {
        data = new Gson() fromJson(x, classOf[FeedClickData])
      } catch {
        case _: Exception => {}
      }
      data
    }.filter(x => x.user_id > 0 && x.feed_id > 0 && "VISIT".equalsIgnoreCase(x.subkey)).map(x => {
      val put = new Put(rowKey(x))
      put.addColumn(Bytes.toBytes("c"), Bytes.toBytes("feedType"), Bytes.toBytes(x.feed_type))
      (new ImmutableBytesWritable, put)
    }).saveAsHadoopDataset(jobConf)
  }


  def main(args: Array[String]): Unit = {
    //    var startDay = FastDateFormat.getInstance("yyyyMMdd").parse("20200301")
    //    val endDay = FastDateFormat.getInstance("yyyyMMdd").parse("20200425")
    //    while (startDay.compareTo(endDay) <= 0) {
    //      val filename = String.format("/tmp/carl/%s.json/part-00000", FastDateFormat.getInstance("yyyyMMdd").format(startDay))
    //      readSingleFile(sc, filename)
    //      startDay = DateUtils.addDays(startDay, 1)
    //    }

    // 统计数量
    //    @transient val hbaseConf = HBaseConfiguration.create()
    //    hbaseConf.set("hbase.zookeeper.quorum", "")
    //    hbaseConf.set(TableInputFormat.INPUT_TABLE, "feed_click_event")
    //    sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result]).count()
    //  }
  }
}
