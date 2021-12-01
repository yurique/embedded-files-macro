logLevel := Level.Warn

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.7.0")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.1.0")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.3")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.10")

addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.20")

addSbtPlugin("com.codecommit" % "sbt-github-actions" % "0.14.0")

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.5.9")

addSbtPlugin("ch.epfl.scala" % "sbt-version-policy" % "2.0.0")
