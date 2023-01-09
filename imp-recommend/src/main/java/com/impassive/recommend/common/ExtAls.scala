package com.impassive.recommend.common

import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, Rating}

import scala.collection.mutable
import org.apache.spark.mllib.linalg.DenseMatrix
import org.apache.spark.mllib.rdd.MLPairRDDFunctions.fromPairRDD
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.jblas.DoubleMatrix

/**
 * Support more tuning feature for LAS model: org.apache.spark.mllib.recommendation.MatrixFactorizationModel
 */

object ExtAls {

  private def recommendForAll(
                               rank: Int,
                               srcFeatures: RDD[(Int, Array[Double])],
                               dstFeatures: RDD[(Int, Array[Double])],
                               num: Int,
                               blockSize: Int = 4096,
                               blockCacheLevel: StorageLevel = StorageLevel.NONE): RDD[(Int, Array[(Int, Double)])] = {

    val srcBlocks = blockify(rank, srcFeatures, blockSize).setName("srcBlocks")
    if (StorageLevel.NONE != blockCacheLevel) { //force cache
      srcBlocks.persist(blockCacheLevel)
      srcBlocks.count()
    }

    def foreachActive(m: DenseMatrix, f: (Int, Int, Double) => Unit): Unit = {
      if (!m.isTransposed) {
        // outer loop over columns
        var j = 0
        while (j < m.numCols) {
          var i = 0
          val indStart = j * m.numRows
          while (i < m.numRows) {
            f(i, j, m.values(indStart + i))
            i += 1
          }
          j += 1
        }
      } else {
        // outer loop over rows
        var i = 0
        while (i < m.numRows) {
          var j = 0
          val indStart = i * m.numCols
          while (j < m.numCols) {
            f(i, j, m.values(indStart + j))
            j += 1
          }
          i += 1
        }
      }
    }

    val ratings = srcBlocks.cartesian(srcBlocks).flatMap {
      case ((srcIds, srcFactors), (dstIds, dstFactors)) =>
        val m = srcIds.length
        val n = dstIds.length


        val ratings = srcFactors.transpose.multiply(dstFactors)
        val output = new Array[(Int, (Int, Double))](m * n)
        var k = 0
        foreachActive(ratings, { (i, j, r) =>
          output(k) = (srcIds(i), (dstIds(j), r))
          k += 1
        })
        output.toSeq
    }
    ratings.topByKey(num)(Ordering.by(_._2))
  }

  private def blockify(
                        rank: Int,
                        features: RDD[(Int, Array[Double])],
                        blockSize: Int = 4096): RDD[(Array[Int], DenseMatrix)] = {
    val blockStorage = rank * blockSize
    features.mapPartitions { iter =>
      iter.grouped(blockSize).map { grouped =>
        val ids = mutable.ArrayBuilder.make[Int]
        ids.sizeHint(blockSize)
        val factors = mutable.ArrayBuilder.make[Double]
        factors.sizeHint(blockStorage)
        var i = 0
        grouped.foreach {
          case (id, factor) =>
            ids += id
            factors ++= factor
            i += 1
        }
        (ids.result(), new DenseMatrix(rank, i, factors.result()))
      }
    }
  }



  def cosineSimilarity(vec1: DoubleMatrix, vec2: DoubleMatrix): Double = {
    vec1.dot(vec2) / (vec1.norm2() * vec2.norm2())
  }

}
