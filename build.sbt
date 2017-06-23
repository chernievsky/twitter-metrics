name := "twitter-example"

version := "1.0"

scalaVersion := "2.11.11"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  // Utils
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe" % "config" % "1.3.1",

  // Scrooge
  // Exclusion rule required to evict the com.twitter libthrift library.
  "org.apache.thrift" % "libthrift" % "0.9.2",
  "com.twitter" %% "scrooge-core" % "4.18.0" exclude("com.twitter", "libthrift"),
  "com.twitter" %% "finagle-thrift" % "6.45.0" exclude("com.twitter", "libthrift"),
  "net.debasishg" %% "redisclient" % "3.4",

  // Twitter API client
  "com.danielasfregola" %% "twitter4s" % "5.1"
)

// here the directory with thrift files is specified
// generate scala files with `sbt scroogeGen` or `sbt compile` (the latter will do much more)
// they will be stored in `targed/src_managed` directory and should be visible from IDE
// right after the execution of above commands (but it may not work on some older versions of Intellij Idea)
scroogeThriftSourceFolder in Compile <<= baseDirectory {
  base => base / "src" / "main" / "thrift"
}
