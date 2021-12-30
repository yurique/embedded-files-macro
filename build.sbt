inThisBuild(
  List(
    organization                        := "com.yurique",
    homepage                            := Some(url("https://github.com/yurique/embedded-files-macro")),
    licenses                            := List("MIT" -> url("https://github.com/yurique/embedded-files-macro/blob/main/LICENSE.md")),
    developers                          := List(Developer("yurique", "Iurii Malchenko", "i@yurique.com", url("https://github.com/yurique"))),
    scmInfo                             := Some(ScmInfo(url("https://github.com/yurique/embedded-files-macro"), "scm:git@github.com/yurique/embedded-files-macro.git")),
    Test / publishArtifact              := false,
    versionScheme                       := Some("early-semver"),
    scalaVersion                        := ScalaVersions.v213,
    crossScalaVersions                  := Seq(
      ScalaVersions.v3,
      ScalaVersions.v213,
      ScalaVersions.v212
    ),
    versionPolicyIntention              := Compatibility.BinaryCompatible,
    githubWorkflowBuild += WorkflowStep.Sbt(List("versionPolicyCheck")),
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowPublishTargetBranches := Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
    githubWorkflowPublish               := Seq(WorkflowStep.Sbt(List("ci-release"))),
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
    .settings(
      libraryDependencies ++=
        CrossVersion.partialVersion(scalaVersion.value).collect { case (2, _) =>
          "org.scala-lang" % "scala-reflect" % scalaVersion.value
        },
      libraryDependencies ++= Seq(
        "junit"           % "junit"           % "4.13.2" % Test,
        ("com.github.sbt" % "junit-interface" % "0.13.3" % Test)
          .exclude("junit", "junit-dep")
      ),
      testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")
    )

lazy val noPublish = Seq(
  publishLocal / skip := true,
  publish / skip      := true,
  publishTo           := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))
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
