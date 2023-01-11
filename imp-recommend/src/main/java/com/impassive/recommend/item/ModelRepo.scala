package com.impassive.recommend.item

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods.{render, _}
import org.json4s.{DefaultFormats, _}

@SerialVersionUID(1L)
object ModelRepo extends Serializable {

  private val PATH = "/rec/model"

  private val thisFormatVersion = "1.0"

  private val thisClassName = "com.impassive.recommend.feed.ModelRepo"

  private def dataPath(path: String): String = new Path(path, "data").toUri.toString

  private def metadataPath(path: String): String = new Path(path, "metadata").toUri.toString

  private def userPath(path: String): String = {
    new Path(dataPath(path), "user").toUri.toString
  }

  private def productPath(path: String): String = {
    new Path(dataPath(path), "product").toUri.toString
  }

  def save(
            sc: SparkContext,
            spark: SparkSession,
            userFeatures: RDD[(Int, Array[Double])],
            productFeatures: RDD[(Int, Array[Double])],
            rank: Int
          ): Unit = {

    /*清空一波数据*/
    val hdfsCnf = sc.hadoopConfiguration
    val hdfs = org.apache.hadoop.fs.FileSystem.get(hdfsCnf)
    val hdfsPath = new Path(PATH)
    if (hdfs.exists(hdfsPath)) {
      hdfs.delete(hdfsPath, true)
      hdfs.mkdirs(hdfsPath)
    }

    val metadata = compact(render(
      ("class" -> thisClassName) ~ ("version" -> thisFormatVersion) ~ ("rank" -> rank)))
    import spark.implicits._
    sc.parallelize(Seq(metadata), 1).saveAsTextFile(metadataPath(PATH))
    userFeatures.toDF("id", "features").write.parquet(userPath(PATH))
    productFeatures.toDF("id", "features").write.parquet(productPath(PATH))
  }

  /**
   * Load metadata from the given path.
   *
   * @return (class name, version, metadata)
   */
  private def loadMetadata(sc: SparkContext, path: String): (String, String, JValue) = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val metadata = parse(sc.textFile(metadataPath(path)).first())
    val clazz = (metadata \ "class").extract[String]
    val version = (metadata \ "version").extract[String]
    (clazz, version, metadata)
  }

  def load(sc: SparkContext,
           spark: SparkSession
          ): (RDD[(Int, Array[Double])], RDD[(Int, Array[Double])], Int) = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val (className, formatVersion, metadata) = loadMetadata(sc, PATH)
    assert(className == thisClassName)
    assert(formatVersion == thisFormatVersion)
    val rank = (metadata \ "rank").extract[Int]
    val userFeatures = spark.read.parquet(userPath(PATH)).rdd.map {
      case Row(id: Int, features: Seq[_]) =>
        (id, features.asInstanceOf[Seq[Double]].toArray)
    }
    val productFeatures = spark.read.parquet(productPath(PATH)).rdd.map {
      case Row(id: Int, features: Seq[_]) =>
        (id, features.asInstanceOf[Seq[Double]].toArray)
    }
    (userFeatures, productFeatures, rank)
  }
}
