buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath(Plugins.Android.gradle)
    classpath(Plugins.Kotlin.gradle)
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  id(Plugins.KTlint.id) version Plugins.KTlint.version
}

subprojects {
  apply(plugin = Plugins.KTlint.id) // Version is inherited from parent

  ktlint {
    coloredOutput.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    disabledRules.set(setOf("no-wildcard-imports"))

    filter {
      exclude("**/generated/**", "**/resources/MR.kt")
      include("**/kotlin/**")
    }
  }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
  kotlinOptions {
    allWarningsAsErrors = true
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
