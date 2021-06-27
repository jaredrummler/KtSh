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


object Plugins {

    object Android {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    }

    object Kotlin {
        const val gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Jetbrains {

        /**
         * See: https://github.com/Kotlin/dokka
         */
        object Dokka {
            const val version = "1.4.10.2"
            const val id = "org.jetbrains.dokka"
        }
    }

    /**
     * https://github.com/jlleitschuh/ktlint-gradle
     */
    object KTlint {
        const val id = "org.jlleitschuh.gradle.ktlint"
        const val version = "9.4.0"
    }
}