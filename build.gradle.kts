buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.Android.gradle)
        classpath(Plugins.Kotlin.gradle)
        classpath(Plugins.Jetbrains.Dokka.gradle)
        classpath(Plugins.NexusPublish.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id(Plugins.Jetbrains.Dokka.id) version Plugins.Jetbrains.Dokka.version
    id(Plugins.NexusPublish.id) version Plugins.NexusPublish.version
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
            exclude("**/generated/**")
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

if (Publish.applyPlugin) {
    apply(plugin = Plugins.NexusPublish.id)
    apply(from = "$rootDir/scripts/publish-root.gradle")
}
