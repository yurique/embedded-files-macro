import sbt.Keys._
import sbt._

object ScalaOptions {

  val fixOptions = Seq(
    scalacOptions ~= (_.filterNot(
      Set(
        "-source",
        "future"
      )
    )),
    scalacOptions ++= CrossVersion.partialVersion(scalaVersion.value).collect { case (2, _) =>
      "-language:experimental.macros"
    },
    (Compile / doc / scalacOptions) ~= (_.filterNot(
      Set(
        "-scalajs",
        "-deprecation",
        "-explain-types",
        "-explain",
        "-feature",
        "-language:existentials,experimental.macros,higherKinds,implicitConversions",
        "-unchecked",
        "-Xfatal-warnings",
        "-Ykind-projector",
        "-from-tasty",
        "-encoding",
        "utf8"
      )
    ))
  )
}
