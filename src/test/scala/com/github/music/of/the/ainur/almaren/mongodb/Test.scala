package com.github.music.of.the.ainur.almaren.mongodb

import org.apache.spark.sql.SaveMode
import org.scalatest._
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit
import org.apache.spark.sql.{DataFrame, Column}
import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.functions._
import org.scalatest.funsuite.AnyFunSuite

class  Test extends AnyFunSuite with BeforeAndAfter {

  val almaren = Almaren("App Test")

  val spark = almaren.spark
    .master("local[*]")
    .config("spark.ui.enabled","false")
    .config("spark.sql.shuffle.partitions", "1")
    .getOrCreate()
  
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  val testTable = "movies"
  createSampleData(testTable)

  //Write Data From MongoDB
  val df1 = almaren.builder.sourceSql(s"SELECT * FROM $testTable").targetMongoDb("localhost","test","movie", saveMode = SaveMode.Overwrite).batch

  // Read Data From MongoDB
  val df2 = almaren.builder.sourceMongoDb("localhost","test","movie").batch

  test(df1,df2,"MongoDB")

  def test(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    testCount(df1, df2, name)
    testCompare(df1, df2, name)
  }

  def testCount(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    val count1 = df1.count()
    val count2 = df2.count()
    val count3 = spark.emptyDataFrame.count()
    test(s"Count Test:$name should match") {
      assert(count1 == count2)
    }
    test(s"Count Test:$name should not match") {
      assert(count1 != count3)
    }
  }

  // Doesn't support nested type and we don't need it :)
  def testCompare(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    val diff = compare(df1, df2)
    test(s"Compare Test:$name should be zero") {
      assert(diff == 0)
    }
    test(s"Compare Test:$name, should not be able to join") {
      assertThrows[AnalysisException] {
        compare(df2, spark.emptyDataFrame)
      }
    }
  }

  private def compare(df1: DataFrame, df2: DataFrame): Long =
    df1.as("df1").join(df2.as("df2"), joinExpression(df1), "leftanti").count()

  private def joinExpression(df1: DataFrame): Column =
    df1.schema.fields
      .map(field => col(s"df1.${field.name}") <=> col(s"df2.${field.name}"))
      .reduce((col1, col2) => col1.and(col2))


  def createSampleData(tableName: String): Unit = {
    val json_str = scala.io.Source.fromURL(getClass.getResource("/sample_data/movies.json")).mkString
    val res = spark.read.json(Seq(json_str).toDS)
    res.createTempView(tableName)
  }


}
