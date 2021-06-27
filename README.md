# KtSh

<img src=".github/ktsh-demo.gif" align="left" hspace="10" vspace="10"></a>

**An open source library to execute shell commands on Android or the JVM, written in Kotlin.**

<a target="_blank" href="LICENSE"><img src="http://img.shields.io/:license-apache-blue.svg" alt="License" /></a>
<a target="_blank" href="https://travis-ci.org/jaredrummler/KtSh"><img src="https://travis-ci.org/jaredrummler/KtSh.svg?branch=master" alt="Build Status" /></a>
<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/ktsh"><img src="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/ktsh/badge.svg" alt="Maven Central" /></a>
<a target="_blank" href="https://twitter.com/jaredrummler"><img src="https://img.shields.io/twitter/follow/jaredrummler.svg?style=social" /></a>

# Downloads

Download [the latest JAR](https://repo1.maven.org/maven2/com/jaredrummler/ktsh/1.0.0/ktsh-1.0.0.jar) or grab via Gradle:

```groovy
implementation 'com.jaredrummler:ktsh:1.0.0'
```

Alternatively, you can simply copy the `Shell.kt` file to your project and update the package name.

# Usage

```kotlin
val shell = Shell("sh")                         // create a shell
val result = shell.run("echo 'Hello, World!'")  // execute a command
if (result.isSuccess) {                         // check if the exit-code was 0
    println(result.stdout())                    // prints "Hello, World!"
}
```

License
-------

    Copyright (C) 2021 Jared Rummler

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
