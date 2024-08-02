// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
plugins {
  `kotlin-dsl`
  alias(libs.plugins.spotless)
  alias(libs.plugins.nexus.publish)
}

java { toolchain { languageVersion = JavaLanguageVersion.of(17) } }

spotless {
  kotlin {
    target("src/**/*.kt")
    ktlint()
    licenseHeaderFile(rootProject.file("../spotless/copyright.txt"))
  }

  kotlinGradle {
    target("*.kts")
    ktlint()
    licenseHeaderFile(rootProject.file("../spotless/copyright.txt"), "(^(?![\\/ ]\\**).*$)")
  }
}

gradlePlugin {
  plugins {
    register("root") {
      id = "convention.plugin.root"
      implementationClass = "com.addhen.gradle.convention.plugin.RootConventionPlugin"
    }

    register("nexusPublication") {
      id = "convention.plugin.nexus.publication"
      implementationClass = "com.addhen.gradle.convention.plugin.NexusPublicationConventionPlugin"
    }

    register("mavenPublication") {
      id = "convention.plugin.maven.publication"
      implementationClass = "com.addhen.gradle.convention.plugin.MavenPublicationConventionPlugin"
    }

    register("spotlessConvention") {
      id = "convention.plugin.spotless"
      implementationClass = "com.addhen.gradle.convention.plugin.SpotlessConventionPlugin"
    }
  }
}

dependencies {
  implementation(libs.nexus.publish.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.spotless.gradlePlugin)
  compileOnly(libs.metalava.gradlePlugin)
}
