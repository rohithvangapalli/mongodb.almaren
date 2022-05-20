package com.github.music.of.the.ainur.almaren.mongodb

import org.apache.spark.sql.{DataFrame,SaveMode}
import com.github.music.of.the.ainur.almaren.Tree
import com.github.music.of.the.ainur.almaren.builder.Core
import com.github.music.of.the.ainur.almaren.state.core.{Target,Source}
import com.mongodb.spark._

private[almaren] case class SourceMongoDb(
  hosts: String,
  database: String,
  collection: String,
  user:Option[String],
  password:Option[String],
  stringPrefix:Option[String],
  options:Map[String,String]) extends Source {

  def source(df: DataFrame): DataFrame = {
    logger.info(s"hosts:{$hosts}, database:{$database}, collection:{$collection}, user:{$user}, options:{$options}")
    val paramsOptions = (user, password, stringPrefix) match {
      case (Some(u), Some(p), Some(c)) => Map("uri" -> s"mongodb+$c://$u:$p@$hosts/$database.$collection")
      case (Some(u), Some(p), _) => Map("uri" -> s"mongodb://$u:$p@$hosts/$database.$collection")
      case (_, _, Some(c)) => Map("uri" -> s"mongodb+$c://$hosts/$database.$collection")
      case (_, _, _) => Map("uri" -> s"mongodb://$hosts/$database.$collection")
    }
    val params = paramsOptions ++ Map("database" -> database, "collection" -> collection) ++ options

    df.sparkSession.read.format("mongodb")
      .options(params)
      .load()
  }
}


private[almaren] case class TargetMongoDb(
  hosts: String,
  database: String,
  collection: String,
  user:Option[String],
  password:Option[String],
  stringPrefix:Option[String],
  options:Map[String,String],
  saveMode:SaveMode) extends Target {

  def target(df: DataFrame): DataFrame = {
    logger.info(s"hosts:{$hosts}, database:{$database}, collection:{$collection}, user:{$user}, options:{$options}, saveMode:{$saveMode}")

    val paramsOptions = (user, password, stringPrefix) match {
      case (Some(u), Some(p), Some(c)) => Map("uri" -> s"mongodb+$c://$u:$p@$hosts/$database.$collection")
      case (Some(u), Some(p), _) => Map("uri" -> s"mongodb://$u:$p@$hosts/$database.$collection")
      case (_, _, Some(c)) => Map("uri" -> s"mongodb+$c://$hosts/$database.$collection")
      case (_, _, _) => Map("uri" -> s"mongodb://$hosts/$database.$collection")
    }
    val params = paramsOptions ++ Map("database" -> database, "collection" -> collection) ++ options

    df.write.format("mongodb")
      .options(params)
      .mode(saveMode)
      .save
    df
  }

}

private[almaren] trait MongoDbConnector extends Core {
  def targetMongoDb(hosts: String,database: String,collection: String,user:Option[String] = None,password:Option[String] = None,stringPrefix:Option[String] = None,options:Map[String,String] = Map(),saveMode:SaveMode = SaveMode.ErrorIfExists): Option[Tree] =
     TargetMongoDb(hosts,database,collection,user,password,stringPrefix,options,saveMode)

  def sourceMongoDb(hosts: String,database: String,collection: String,user:Option[String] = None,password:Option[String] = None,stringPrefix:Option[String] = None,options:Map[String,String] = Map()): Option[Tree] =
    SourceMongoDb(hosts,database,collection,user,password,stringPrefix,options)
}

object MongoDb {
  implicit class MongoImplicit(val container: Option[Tree]) extends MongoDbConnector
}
