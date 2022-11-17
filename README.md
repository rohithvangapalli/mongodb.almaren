# MongoDB Connector

[![Build Status](https://github.com/music-of-the-ainur/mongodb.almaren/actions/workflows/mongodb-almaren-githubactions.yml/badge.svg)](https://github.com/music-of-the-ainur/mongodb.almaren/actions/workflows/mongodb-almaren-githubactions.yml)

To add Mongodb Almaren dependency to your sbt build:

```
libraryDependencies += "com.github.music-of-the-ainur" %% "mongodb-almaren" % "0.0.7-3.3"
libraryDependencies += "org.mongodb.spark" % "mongo-spark-connector" % "10.0.4"
```

To run in spark-shell:

```
spark-shell --master local[*] --packages "com.github.music-of-the-ainur:almaren-framework_2.12:0.9.8-3.3,com.github.music-of-the-ainur:mongodb-almaren_2.12:0.0.7-3.3,org.mongodb.spark:mongo-spark-connector:10.0.4"
```

MongoDB Connector is available in [Maven Central](https://mvnrepository.com/artifact/com.github.music-of-the-ainur)
repository.

| version                    | Connector Artifact                                             |
|----------------------------|----------------------------------------------------------------|
| Spark 3.3.x and scala 2.12 | `com.github.music-of-the-ainur:mongodb-almaren_2.12:0.0.7-3.3` |
| Spark 3.2.x and scala 2.12 | `com.github.music-of-the-ainur:mongodb-almaren_2.12:0.0.7-3.2` |
| Spark 3.1.x and scala 2.12 | `com.github.music-of-the-ainur:mongodb-almaren_2.12:0.0.7-3.1` |
| Spark 2.4.x and scala 2.12 | `com.github.music-of-the-ainur:mongodb-almaren_2.12:0.0.7-2.4` |
| Spark 2.4.x and scala 2.11 | `com.github.music-of-the-ainur:mongodb-almaren_2.11:0.0.7-2.4` |


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

almaren.builder.sourceMongoDbUri("localhost","foo","bar",None,None,Some("srv"))
```

Parameters for Uri:

| Parameters | Description                                                                                                       |
|------------|-------------------------------------------------------------------------------------------------------------------|
| uri        | mongodb://localhost:27017/foo                                                                                     |
| collection | bar                                                                                                               |
| options    | extra connector options                                                                                           |

#### For Connection Uri Type Mongo

```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit

almaren.builder.sourceMongoDb("mongodb://localhost:27017/foo","bar")
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

Parameters for Uri:

| Parameters | Description                                                                                                       |
|------------|-------------------------------------------------------------------------------------------------------------------|
| uri        | mongodb://localhost:27017/foo                                                                                     |
| collection | bar                                                                                                               |
| options    | extra connector options                                                                                           |
| saveMode   | SaveMode.Overwrite      |


#### For Connection Uri Type Mongo

```scala
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.mongodb.MongoDb.MongoImplicit

almaren.builder.targetMongoDbUri("mongodb://localhost:27017/foo","bar", saveMode = SaveMode.Overwrite)
```