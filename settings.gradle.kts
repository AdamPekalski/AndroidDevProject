pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        // Android Gradle Plugin (this matches what your Studio was using)
        id("com.android.application") version "8.13.1"

        // Kotlin 2.0.21 (meets brief: 2.0.21 or higher)
        id("org.jetbrains.kotlin.android") version "2.0.21"

        // Compose compiler plugin for Kotlin 2.0.21
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"

        // KSP plugin compatible with Kotlin 2.0.21
        id("com.google.devtools.ksp") version "2.0.21-1.0.26"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "movemate"
include(":app")
