import sbtassembly.AssemblyPlugin.autoImport._
import sbt.Attributed._
import sbtsparksubmit.SparkSubmitPlugin.autoImport._

name := "LightSourceBDAServices"
 
version := "1.0"
 
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "2.0.1" % "provided",
    "org.apache.spark" %% "spark-sql" % "2.0.1"
)

SparkSubmit.settings