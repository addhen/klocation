// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.gradle.convention

import io.github.gradlenexus.publishplugin.NexusPublishExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureNexusPublish() {
  with(pluginManager) { apply("io.github.gradle-nexus.publish-plugin") }

  allprojects {
    group = "com.addhen.klocation"
    version = "0.0.1"
  }

  nexusPublishing {
    // Configure maven central repository
    // https://github.com/gradle-nexus/publish-plugin#publishing-to-maven-central-via-sonatype-ossrh
    repositories {
      sonatype { // only for users registered in Sonatype after 24 Feb 2021
        nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
        snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
      }
    }
  }
}

private fun Project.nexusPublishing(action: NexusPublishExtension.() -> Unit) =
  extensions.configure<NexusPublishExtension>(action)
