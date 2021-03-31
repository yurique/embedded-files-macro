inThisBuild(
  List(
    organization := "com.yurique",
    homepage := Some(url("https://github.com/yurique/embedded-files-macro")),
    licenses := List("MIT" -> url("https://github.com/yurique/embedded-files-macro/blob/main/LICENSE.md")),
    developers := List(Developer("yurique", "Iurii Malchenko", "i@yurique.com", url("https://github.com/yurique"))),
    scmInfo := Some(ScmInfo(url("https://github.com/yurique/embedded-files-macro"), "scm:git@github.com/yurique/embedded-files-macro.git")),
    Test / publishArtifact := false,
    versionScheme := Some("early-semver"),
    scalaVersion := ScalaVersions.v213,
    crossScalaVersions := Seq(
      ScalaVersions.v3RC1,
      ScalaVersions.v213,
      ScalaVersions.v212
    ),
    versionPolicyIntention := Compatibility.BinaryCompatible,
    //  https://github.com/scalacenter/sbt-version-policy/issues/62
    //    githubWorkflowBuild += WorkflowStep.Sbt(List("versionPolicyCheck")),
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowPublishTargetBranches := Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
    githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"))),
    githubWorkflowEnv ~= (_ ++ Map(
      "PGP_PASSPHRASE"    -> s"$${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET"        -> s"$${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> s"$${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> s"$${{ secrets.SONATYPE_USERNAME }}"
    ))
  )
)

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
      )),
      libraryDependencies ++=
        (CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, _)) => Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
          case _            => Seq()
        }),
      libraryDependencies ++= Seq(
        "junit"         % "junit"           % "4.13.2" % Test,
        ("com.novocode" % "junit-interface" % "0.11"   % Test)
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
