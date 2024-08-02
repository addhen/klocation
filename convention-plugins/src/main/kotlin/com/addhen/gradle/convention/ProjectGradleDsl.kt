package com.addhen.gradle.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import java.util.Optional

val Project.libs get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.library(name: String): MinimalExternalModuleDependency {
  return findLibrary(name).get().get()
}

fun DependencyHandlerScope.implementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) {
  add("implementation", artifact.get())
}

fun DependencyHandlerScope.lintChecks(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) {
  add("lintChecks", artifact.get())
}

fun DependencyHandlerScope.implementationBundle(
  artifact: Optional<Provider<ExternalModuleDependencyBundle>>,
) {
  add("implementation", artifact.get())
}

fun DependencyHandlerScope.debugImplementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) {
  add("debugImplementation", artifact.get())
}

fun DependencyHandlerScope.androidTestImplementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) {
  add("androidTestImplementation", artifact.get())
}

fun DependencyHandlerScope.testImplementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) {
  add("testImplementation", artifact.get())
}

private fun DependencyHandlerScope.api(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) {
  add("api", artifact.get())
}
