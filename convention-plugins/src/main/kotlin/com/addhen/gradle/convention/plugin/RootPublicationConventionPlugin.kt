package com.addhen.gradle.convention.plugin

import com.addhen.gradle.convention.configureNexusPublish
import org.gradle.api.Plugin
import org.gradle.api.Project

class RootPublicationConventionPlugin: Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    configureNexusPublish()
  }
}
