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

URI to connect to MongoDB:

```
mongodb://[username:password@]host1[:port1][,...hostN[:portN]][/[defaultauthdb][?options]]
```

## Example

Source
```scala
almaren.builder.sourceMongoDb("localhost","foo","movie")
```

Target
```scala
almaren.builder.targetMongoDb("localhost","baz","movie")
```
