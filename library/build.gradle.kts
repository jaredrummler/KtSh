plugins {
    `java-library`
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(Dependencies.Kotlin.stdlib)
    testImplementation(Dependencies.Testing.junit)
}

if (Publish.applyPlugin) {
    apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")
}
