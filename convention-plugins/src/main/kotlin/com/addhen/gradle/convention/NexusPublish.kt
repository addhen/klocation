// Copyright 2024, Addhen Ltd and the k-location project contributors
// SPDX-License-Identifier: Apache-2.0
package com.addhen.gradle.convention

import io.github.gradlenexus.publishplugin.NexusPublishExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureNexusPublish() {
  with(pluginManager) { apply("io.github.gradle-nexus.publish-plugin") }
  val klocationVersion = libs.version("klocation").requiredVersion
  allprojects {
    group = "com.addhen"
    version = klocationVersion
  }

  nexusPublishing {
    // Configure maven central repository
    // https://github.com/gradle-nexus/publish-plugin#publishing-to-maven-central-via-sonatype-ossrh
    repositories {
      sonatype()
    }
  }
}

private fun Project.nexusPublishing(action: NexusPublishExtension.() -> Unit) =
  extensions.configure<NexusPublishExtension>(action)
