ThisBuild / scalaVersion := ScalaVersions.v3M3
ThisBuild / crossScalaVersions := Seq(ScalaVersions.v3RC1, ScalaVersions.v3M3, ScalaVersions.v213, ScalaVersions.v212)

lazy val `embedded-files-macro` =
  crossProject(JVMPlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("macro"))
    .jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))
    .settings(addSharedScalaSourceDir)
    .settings(
      scalacOptions ++=
        (CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, _)) => Seq("-language:experimental.macros")
          case Some((3, _)) => Seq()
          case _            => Seq()
        }),
      scalacOptions in (Compile, doc) ~= (_.filter(_.startsWith("-Xplugin"))),
      libraryDependencies ++=
        (CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, _)) => Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
          case _            => Seq()
        }),
      libraryDependencies ++= Seq(
        "junit"         % "junit"           % "4.11" % Test,
        ("com.novocode" % "junit-interface" % "0.11" % Test)
          .exclude("junit", "junit-dep")
      ),
      testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")
    )

// TODO figure out a better way of doing this
lazy val addSharedScalaSourceDir = Seq(
  Compile / unmanagedSourceDirectories ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) => Seq(baseDirectory.value / "../src/main/scala-2")
    case Some((3, _)) => Seq(baseDirectory.value / "../src/main/scala-3")
    case _            => Seq()
  }),
  Test / unmanagedSourceDirectories ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) => Seq(baseDirectory.value / "../src/test/scala-2")
    case Some((3, _)) => Seq(baseDirectory.value / "../src/test/scala-3")
    case _            => Seq()
  })
)

lazy val noPublish = Seq(
  publishLocal / skip := true,
  publish / skip := true,
  publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "embedded-files-macro"
  )
  .settings(noPublish)
  .aggregate(
    `embedded-files-macro`.jvm,
    `embedded-files-macro`.js
  )
