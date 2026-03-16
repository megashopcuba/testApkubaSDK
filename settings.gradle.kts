pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }

        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.megashopcuba.com/releases")
        maven { url = uri("https://jitpack.io") }

    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repo.megashopcuba.com/releases")
        maven { url = uri("https://jitpack.io") }

    }
}

rootProject.name = "App de prueba de ApkubaSDK"
include(":app")
 