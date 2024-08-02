// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0

import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
  id("convention.plugin.root")
  id("convention.plugin.nexus.publication")
  // trick: for the same plugin versions in all sub-modules
  alias(libs.plugins.android.library).apply(false)
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.spotless) apply false
  alias(libs.plugins.metalava) apply false

  `maven-publish`
  signing
}

publishing {
  // Configure all publications
  publications.withType<MavenPublication> {
    // Stub javadoc.jar artifact
    artifact(tasks.register("${name}JavadocJar", Jar::class) {
      archiveClassifier.set("javadoc")
      archiveAppendix.set(this@withType.name)
    })

    // Provide artifacts information required by Maven Central
    pom {
      name.set("Kotlin Multiplatform library template")
      description.set("Dummy library to test deployment to Maven Central")
      url.set("https://github.com/Kotlin/multiplatform-library-template")

      licenses {
        license {
          name.set("MIT")
          url.set("https://opensource.org/licenses/MIT")
        }
      }
      developers {
        developer {
          id.set("JetBrains")
          name.set("JetBrains Team")
          organization.set("JetBrains")
          organizationUrl.set("https://www.jetbrains.com")
        }
      }
      scm {
        url.set("https://github.com/Kotlin/multiplatform-library-template")
      }
    }
  }
}

signing {
  if (project.hasProperty("signing.gnupg.keyName")) {
    useGpgCmd()
    sign(publishing.publications)
  }
}
