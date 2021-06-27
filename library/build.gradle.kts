plugins {
    `java-library`
    id("kotlin")
    id(Plugins.Jetbrains.Dokka.id) version Plugins.Jetbrains.Dokka.version
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Dependencies.Kotlin.stdlib)
    testImplementation(Dependencies.Testing.junit)
}
