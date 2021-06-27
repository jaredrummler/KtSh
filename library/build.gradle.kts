plugins {
    `java-library`
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Dependencies.Kotlin.stdlib)
    testImplementation(Dependencies.Testing.junit)
}

apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")
