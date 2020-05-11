package com.github.music.of.the.ainur.almaren.mongodb

import org.apache.spark.sql.{DataFrame,SaveMode}
import com.github.music.of.the.ainur.almaren.Tree
import com.github.music.of.the.ainur.almaren.builder.Core
import com.github.music.of.the.ainur.almaren.state.core.{Target,Source}
import com.mongodb.spark._
import com.mongodb.spark.config._

private[almaren] case class SourceMongoDb(uri: String, options:Map[String,String]) extends Source {
  def source(df: DataFrame): DataFrame = {
    logger.info(s"uri:{$uri}, options:{$options}")
    df.sparkSession.read.format("mongo")
      .options(options)
      .load
  }
}

private[almaren] case class TargetMongoDb(uri: String, options:Map[String,String],saveMode:SaveMode) extends Target {
  def target(df: DataFrame): DataFrame = {
    logger.info(s"uri:{$uri}, saveMode:{$saveMode}, options:{$options}")
    df.write.format("mongo")
      .options(options)
      .mode(saveMode)
      .save
    df
  }
}

private[almaren] trait MongoDbConnector extends Core {
  def targetMongoDb(uri: String, options:Map[String,String] = Map(),saveMode:SaveMode = SaveMode.ErrorIfExists): Option[Tree] =
     TargetMongoDb(uri,options,saveMode)

  def sourceMongoDb(uri: String, options:Map[String,String] = Map()): Option[Tree] =
    SourceMongoDb(uri,options)
}

object MongoDb {
  implicit class Implicit(val container: Option[Tree]) extends MongoDbConnector
}
