package com.github.music.of.the.ainur.almaren.mongodb

import org.apache.spark.sql.{DataFrame,SaveMode}
import com.github.music.of.the.ainur.almaren.Tree
import com.github.music.of.the.ainur.almaren.builder.Core
import com.github.music.of.the.ainur.almaren.state.core.{Target,Source}
import com.mongodb.spark._
import com.mongodb.spark.config._
import com.mongodb.spark.sql.SparkSessionFunctions

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
    SparkSessionFunctions(df.sparkSession).loadFromMongoDB(ReadConfig(
      (user,password,stringPrefix) match {
        case (Some(u),Some(p),Some(c)) => Map("uri" -> s"mongodb+$c://$u:$p@$hosts/$database.$collection") ++ options
        case (Some(u),Some(p),_) => Map("uri" -> s"mongodb://$u:$p@$hosts/$database.$collection") ++ options
        case (_,_,Some(c)) => Map("uri" -> s"mongodb+$c://$hosts/$database.$collection") ++ options
        case (_,_,_) => Map("uri" -> s"mongodb://$hosts/$database.$collection") ++ options
      })
    )}
}

private[almaren] case class SourceMongoDbx(
  connectionUri: String,
  collection: String,
  options: Map[String, String]) extends Source {
  def source(df: DataFrame): DataFrame = {
    logger.info(s" collection:{$collection}, options:{$options}")
    SparkSessionFunctions(df.sparkSession).loadFromMongoDB(ReadConfig(
      Map("uri" -> connectionUri, "collection" -> "collection") ++ options
    )
    )
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

    val writeConfig = WriteConfig(      
      (user,password,stringPrefix) match {
        case (Some(u),Some(p),Some(c)) => Map("uri" -> s"mongodb+$c://$u:$p@$hosts/$database.$collection") ++ options
        case (Some(u),Some(p),_) => Map("uri" -> s"mongodb://$u:$p@$hosts/$database.$collection") ++ options
        case (_,_,Some(c)) => Map("uri" -> s"mongodb+$c://$hosts/$database.$collection") ++ options
        case (_,_,_) => Map("uri" -> s"mongodb://$hosts/$database.$collection") ++ options
      })

    MongoSpark.save(
      df.write.format("mongo")
      .options(options)
      .mode(saveMode),writeConfig)
    df
  }

}

private[almaren] case class TargetMongoDbx(
  connectionUri: String,
  collection: String,
  options:Map[String,String],
  saveMode:SaveMode) extends Target {

  def target(df: DataFrame): DataFrame = {
    logger.info(s"collection:{$collection}, options:{$options}, saveMode:{$saveMode}")

    val writeConfig = WriteConfig(
      Map("uri" -> connectionUri, "collection" -> "collection") ++ options
    )

    MongoSpark.save(
      df.write.format("mongo")
        .options(options)
        .mode(saveMode),writeConfig)
    df
  }

}

private[almaren] trait MongoDbConnector extends Core {
  def targetMongoDb(hosts: String,database: String,collection: String,user:Option[String] = None,password:Option[String] = None,stringPrefix:Option[String] = None,options:Map[String,String] = Map(),saveMode:SaveMode = SaveMode.ErrorIfExists): Option[Tree] =
     TargetMongoDb(hosts,database,collection,user,password,stringPrefix,options,saveMode)

  def targetMongoDbConnectionUri(connectionUri: String, collection: String, options: Map[String, String] = Map(), saveMode: SaveMode = SaveMode.ErrorIfExists): Option[Tree] =
    TargetMongoDbx(connectionUri, collection, options, saveMode)

  def sourceMongoDb(hosts: String,database: String,collection: String,user:Option[String] = None,password:Option[String] = None,stringPrefix:Option[String] = None,options:Map[String,String] = Map()): Option[Tree] =
    SourceMongoDb(hosts,database,collection,user,password,stringPrefix,options)

  def sourceMongoDbConnectionUri(connectionUri: String, collection: String, options: Map[String, String] = Map()): Option[Tree] =
    SourceMongoDbx(connectionUri, collection, options)
}

object MongoDb {
  implicit class  MongoImplicit(val container: Option[Tree]) extends MongoDbConnector
}
