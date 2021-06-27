/*
 * Copyright (C) 2021 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object Versions {

    // ---------------------------------------------------------------------------------------------
    // --- GRADLE
    // ---

    // https://developer.android.com/studio/releases/gradle-plugin
    const val gradle = "4.2.1+"

    // ---------------------------------------------------------------------------------------------
    // --- KOTLIN
    // ---

    // https://kotlinlang.org/releases.html
    const val kotlin = "1.5.20"

    // ---------------------------------------------------------------------------------------------
    // --- TESTING
    // ---

    // https://mvnrepository.com/artifact/junit/junit/
    const val junit = "4.13.2"

    // ---------------------------------------------------------------------------------------------
    // --- Dependencies for demo project
    // ---

    object AndroidX {
        // https://developer.android.com/jetpack/androidx/releases/core
        const val ktx = "1.5.0"
        // https://developer.android.com/jetpack/androidx/releases/appcompat
        const val appcompat = "1.3.0"
        // https://developer.android.com/jetpack/androidx/releases/constraintlayout
        const val constraintlayout = "2.0.4"
    }

    object Google {
        // https://maven.google.com/web/index.html#com.google.android.material:material
        const val material = "1.3.0"
    }

    // https://github.com/massivemadness/Squircle-IDE
    object BlackSquircle {
        // https://repo1.maven.org/maven2/com/blacksquircle/
        const val editorkit = "2.0.0"
    }
}