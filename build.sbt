Nice.javaProject

organization := "bio4j"

name := "bio4j"

description := "Bio4j abstract model"

bucketSuffix := "era7.com"

javacOptions in (Compile, doc) := Seq()

target in (Compile, doc) := baseDirectory.value / "docs" / "api"
