# MongoDB Connector

[![Build Status](https://travis-ci.com/music-of-the-ainur/mongodb.almaren.svg?branch=master)](https://travis-ci.com/music-of-the-ainur/mongodb.almaren)

Add to your build:
```
libraryDependencies += "com.github.music-of-the-ainur" %% "mongodb-almaren" % "0.0.6-$SPARK_VERSION"
```

Example in Spark Shell
```
spark-shell --master local[*] --packages "com.github.music-of-the-ainur:almaren-framework_2.12:0.9.3-$SPARK_VERSION,com.github.music-of-the-ainur:mongodb-almaren_2.12:0.0.6-$SPARK_VERSION"
```


## Source and Target

Connector was implemented using: [https://github.com/mongodb/mongo-spark](https://github.com/mongodb/mongo-spark).

## Example

### Source

Parameters:

| Parameters | Description             |
|------------|-------------------------|
| hosts      | localhost:27017         |
| database   | foo                     |
| collection | bar                     |
| user       | username                |
| password   | password                |
|stringPrefix| this is used to specify MongoDb Connection type(srv) https://docs.mongodb.com/manual/reference/connection-string/|
| options    | extra connector options |

#### For Standalone Connection Type Mongo

```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit

almaren.builder.sourceMongoDb("localhost","foo","bar")
```

#### For srv Connection Type Mongo

```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit

almaren.builder.sourceMongoDb("localhost","foo","bar",None,None,Some("srv"))
```

### Target

Parameters:

| Parameters | Description             |
|------------|-------------------------|
| hosts      | localhost:27017         |
| database   | foo                     |
| collection | bar                     |
| user       | username                |
| password   | password                |
|stringPrefix| this is used to specify MongoDb Connection type(srv) https://docs.mongodb.com/manual/reference/connection-string/|
| options    | extra connector options |
| saveMode   | SaveMode.Overwrite      |

#### For Standalone Connection Type Mongo

```scala
import org.apache.spark.sql.SaveMode
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit

almaren.builder.targetMongoDb("localhost","foo","bar", saveMode = SaveMode.Overwrite)
```


#### For srv Connection Type Mongo

```scala
import org.apache.spark.sql.SaveMode
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit

almaren.builder.targetMongoDb("localhost","foo","bar",None,None,Some("srv"), saveMode = SaveMode.Overwrite)
```
