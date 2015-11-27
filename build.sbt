name := "socialmeter"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

val coreNlpVersion = "3.5.2"
val sparkVersion = "1.5.2"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"        % sparkVersion,
  "org.apache.spark" %% "spark-streaming"   % sparkVersion,
  "org.apache.spark" %% "spark-streaming-twitter"   % sparkVersion,
  "edu.stanford.nlp" %  "stanford-corenlp"  % coreNlpVersion,
  "edu.stanford.nlp" %  "stanford-corenlp"  % coreNlpVersion classifier "models",
  "edu.stanford.nlp" %  "stanford-corenlp"  % coreNlpVersion classifier "models-chinese",
  "org.scalatest"    %% "scalatest"         % "2.2.5" % "test",
  "org.mockito"      %  "mockito-all"       % "1.9.5" % "test"
)