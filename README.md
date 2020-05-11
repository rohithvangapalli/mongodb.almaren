# MongoDB Connector

[![Build Status](https://travis-ci.com/music-of-the-ainur/mongodb.almaren.svg?branch=master)](https://travis-ci.com/music-of-the-ainur/mongodb.almaren)

Add to your build:
```
libraryDependencies += "com.github.music-of-the-ainur" %% "mongodb-almaren" % "0.0.1-2-4"
```

Example in Spark Shell
```
spark-shell --master local[*] --packages "com.github.music-of-the-ainur:almaren-framework_2.11:0.2.7-$SPARK_VERSION,com.github.music-of-the-ainur:mongodb-almaren_2.11:0.0.1-2-4"
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
| options    | extra connector options |

```scala
almaren.builder.sourceMongoDb("localhost","foo","bar")
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
| options    | extra connector options |
| saveMode   | SaveMode.Overwrite      |


```scala
almaren.builder.targetMongoDb("localhost","foo","bar")
```
