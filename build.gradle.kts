// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.intellij.sdk.pycharm"
version = "1.1.1"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

// See https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1.7")
    type.set("PY")
    plugins.set(listOf("Pythonid"))
    downloadSources.set(false)
}

tasks {
    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        version.set("${project.version}")
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    processResources {
        from("src/main/resources") {
            include("**/*")
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
