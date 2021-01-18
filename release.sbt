ThisBuild / organization := "com.yurique"
ThisBuild / homepage := Some(url("https://github.com/yurique/embedded-files-macro"))
ThisBuild / licenses += "MIT" -> url("https://github.com/yurique/embedded-files-macro/blob/main/LICENSE.md")
ThisBuild / developers += Developer("yurique", "Iurii Malchenko", "i@yurique.com", url("https://github.com/yurique"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/yurique/embedded-files-macro"), "scm:git@github.com/yurique/embedded-files-macro.git"))
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeProfileName := "yurique"
ThisBuild / publishArtifact in Test := false