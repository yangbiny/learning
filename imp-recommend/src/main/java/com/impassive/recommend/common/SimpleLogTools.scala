package com.impassive.recommend.common

import org.apache.spark.SparkContext

@SerialVersionUID(1L)
object SimpleLogTools extends Serializable {


  case class GelfLog(
                      _app: String,
                      level: Int,
                      version: String,
                      host: String,
                      short_message: String,
                      logger_name: String,
                      _applicationId: String
                    )

  def info(msg: String, loggerName: String, sc: SparkContext): Unit = {
    //sendLog(sc.applicationId, msg, loggerName)
  }


}
