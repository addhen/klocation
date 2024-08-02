package com.addhen.gradle.convention.plugin

import com.addhen.gradle.convention.configureMavenPublish
import org.gradle.api.Plugin
import org.gradle.api.Project

class MavenPublicationConventionPlugin: Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    configureMavenPublish()
  }
}
