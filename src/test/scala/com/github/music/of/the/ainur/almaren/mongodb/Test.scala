package com.github.music.of.the.ainur.almaren.mongodb

import org.apache.spark.sql.SaveMode
import org.scalatest._
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit

class Test extends FunSuite with BeforeAndAfter {

  val almaren = Almaren("App Test")

  val spark = almaren.spark
    .master("local[*]")
    .config("spark.ui.enabled","false")
    .config("spark.sql.shuffle.partitions", "1")
    .getOrCreate()
  
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  // Save Twitter data to Solr
  val df = almaren.builder.sourceMongoDb("localhost","foo","movie").batch

  almaren.builder.targetMongoDb("localhost","baz","movie").batch

  df.show(false)
  df.printSchema()

}
