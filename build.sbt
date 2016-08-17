name := "workflow-service"
version := "0.0.1"
scalaVersion := "2.11.0"

lazy val versions = new {
    val finatra = "2.2.0"
}

resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "Twitter Maven" at "https://maven.twttr.com"
)

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % "2.11.0",
    "com.twitter" %% "finagle-http" % "6.36.0",
    "com.twitter" %% "finatra-http" % versions.finatra,
    "com.twitter" %% "finatra-httpclient" % versions.finatra,
    "org.json4s" %% "json4s-native" % "3.4.0"
)
