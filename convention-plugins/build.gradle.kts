plugins {
  `kotlin-dsl`
  alias(libs.plugins.spotless)
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

spotless {
  kotlin {
    target("src/**/*.kt")
    ktlint()
    licenseHeaderFile(rootProject.file("../../spotless/copyright.txt"))
  }

  kotlinGradle {
    target("*.kts")
    ktlint()
    licenseHeaderFile(rootProject.file("../../spotless/copyright.txt"), "(^(?![\\/ ]\\**).*$)")
  }
}

gradlePlugin {
  plugins {
    register("spotlessConvention") {
      id = "convention.plugin.spotless"
      implementationClass = "com.addhen.gradle.convention.plugin.SpotlessConventionPlugin"
    }
  }
}

dependencies {
  implementation(libs.nexus.publish)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.spotless.gradlePlugin)
}
