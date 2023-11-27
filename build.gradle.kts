
plugins {
  id("com.android.library") version "8.1.0"
  id("maven-publish")
}

group = "skip.cmark"

val compileSdkVersionProperty: Int by rootProject.extra
val minSdkVersionProperty: Int by rootProject.extra
val targetSdkVersionProperty: Int by rootProject.extra
val buildToolsVersionProperty: String by rootProject.extra
val ndkVersionProperty: String by rootProject.extra

android {
  namespace = "skip.cmark"
  compileSdk = 34
  buildToolsVersion = "34.0.0"
  ndkVersion = "26.0.10792818"

  defaultConfig {
    minSdk = 29
    ndk { abiFilters.addAll(setOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a")) }
  }

  externalNativeBuild { cmake { path("CMakeLists.txt") } }

  compileOptions {
    targetCompatibility(JavaVersion.VERSION_17)
    sourceCompatibility(JavaVersion.VERSION_17)
  }

  sourceSets {
    named("main") {
      //java.srcDir("java")
      manifest.srcFile("AndroidManifest.xml")
      //res.srcDir("res")
    }
  }

  publishing {
    multipleVariants {
      withSourcesJar()
      withJavadocJar()
      includeBuildTypeValues("debug", "release")
    }
  }
}

dependencies {
  testImplementation("junit:junit:4.12")
}

version =
    if ("USE_SNAPSHOT".byProperty.toBoolean()) {
      "${"VERSION_NAME".byProperty}-SNAPSHOT"
    } else {
      "VERSION_NAME".byProperty.toString()
    }

val String.byProperty: String?
  get() = providers.gradleProperty(this).orNull

publishing {
  publications {
    register<MavenPublication>("default") {
      groupId = project.group.toString()
      artifactId = project.name
      version = project.version.toString()
      afterEvaluate { from(components["default"]) }
      pom {
        description.set(
            "cmark library")
        name.set(project.name)
        url.set("https://github.com/skiptools/skip-cmark.git")
      }
    }
  }
}

