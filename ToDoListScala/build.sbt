ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.17"

lazy val root = (project in file("."))
  .settings(
    name := "Prac5"
  )

// https://mvnrepository.com/artifact/org.scalafx/scalafx
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.192-R14"

// https://mvnrepository.com/artifact/org.scalafx/scalafxml-core-sfx8
libraryDependencies += "org.scalafx" %% "scalafxml-core-sfx8" % "0.5"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

// https://mvnrepository.com/artifact/org.scalikejdbc/scalikejdbc
libraryDependencies += "org.scalikejdbc" %% "scalikejdbc" % "3.4.2"

// https://mvnrepository.com/artifact/org.apache.derby/derby
libraryDependencies += "org.apache.derby" % "derby" % "10.14.2.0"

// https://mvnrepository.com/artifact/com.h2database/h2
libraryDependencies += "com.h2database" % "h2" % "1.4.200"

