import java.util.Properties

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
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {

    val path="env.properties"
    val props= Properties()
    props.load(file(path).reader())
    val bytesafetoken=props.getProperty("bytesafetoken")

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven{
            name="bytesafe"
            url = uri("https://mobts.bytesafe.dev/maven/mobts/")
            credentials{
                username="bytesafe"
                password=bytesafetoken
            }
        }
    }
}

rootProject.name = "Coding Style Guideline"
include(":app")
include(":apiservices")
includeBuild("../ts-component")
 