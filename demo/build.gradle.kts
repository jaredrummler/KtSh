plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.jaredrummler.ktsh.demo"
        minSdk = 28
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    sourceSets {
        map { set ->
            // https://github.com/gradle/kotlin-dsl-samples/issues/443
            set.java.srcDir("src/${set.name}/kotlin")
        }
    }
}

dependencies {
    implementation(project(":library"))
    implementation(Dependencies.Kotlin.stdlib)
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.2")
    implementation(Dependencies.AndroidX.ktx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.constraintlayout)
    implementation(Dependencies.Google.material)
    implementation(Dependencies.BlackSquircle.editor)
    implementation(Dependencies.BlackSquircle.shell)
    testImplementation(Dependencies.Testing.junit)
}
