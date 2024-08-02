package com.addhen.gradle.convention.plugin

import com.addhen.gradle.convention.configureSpotless
import org.gradle.api.Plugin
import org.gradle.api.Project

class SpotlessConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) { configureSpotless() }
  }
}
