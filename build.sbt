name := "q"

version := "1.0"

lazy val `q` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( javaJdbc ,
  javaEbean,
  cache ,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.avaje" % "ebean" % "2.7.2",
  "javax.websocket" % "javax.websocket-api" % "1.1")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  