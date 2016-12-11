# Android Shell

<img src="https://github.com/jrummyapps/android-shell/blob/master/demo/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true" align="left" hspace="10" vspace="10"></a>

Execute shell commands on Android.

<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>
<a target="_blank" href="LICENSE"><img src="http://img.shields.io/:license-apache-blue.svg" alt="License" /></a>
<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.jrummyapps/android-shell"><img src="https://maven-badges.herokuapp.com/maven-central/com.jrummyapps/android-shell/badge.svg" alt="Maven Central" /></a>
<a target="_blank" href="http://www.methodscount.com/?lib=com.jrummyapps%3Aandroid-shell%3A1.0.0"><img src="https://img.shields.io/badge/methods-231-e91e63.svg" /></a>
<a target="_blank" href="http://www.methodscount.com/?lib=com.jrummyapps%3Aandroid-shell%3A1.0.0"><img src="https://img.shields.io/badge/Size-32 KB-e91e63.svg"/></a>
<a target="_blank" href="https://twitter.com/jrummyapps"><img src="https://img.shields.io/twitter/follow/jrummyapps.svg?style=social" /></a>

Download [the latest AAR](https://repo1.maven.org/maven2/com/jrummyapps/android-shell/1.0.0/android-processes-1.0.0.aar) or grab via Gradle:

```groovy
compile 'com.jrummyapps:android-shell:1.0.0'
```
<br>


Usage
-----

Common utility functions include:

```java
CommandResult Shell.run(String shell, String... commands)
CommandResult Shell.SH.run(String... commands)
CommandResult Shell.SU.run(String... commands)
```

The result will contains the exit code, standard output (stdout), and standard error (stderr).

Example of running a command as root:

```java
CommandResult result = Shell.SU.run("id");
if (result.isSuccessful()) {
  System.out.println(result.getStdout());
  // Example output on a rooted device:
  // uid=0(root) gid=0(root) groups=0(root) context=u:r:init:s0
}
```

You can open multiple shell instances using `Shell.Builder` or `Shell.Console.Builder`. Calling `Shell.SU.run(String... commands)` will keep a su session open in the background so additional calls will not create new superuser requests.

Fore more information please see http://su.chainfire.eu/

Acknowledgements
----------------

[libsuperuser](https://github.com/Chainfire/libsuperuser) by [Chainfire](https://twitter.com/ChainfireXDA)

License
-------

    Copyright (C) 2016 JRummy Apps Inc.
    Copyright (C) 2012-2015 Jorrit "Chainfire" Jongma

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
