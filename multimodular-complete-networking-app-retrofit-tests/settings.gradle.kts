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
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MultimodularCompleteRetrofitEmailApp"
include(":app")
include(":data")
include(":domain")
include(":core")
include(":ui")
include(":navigation")
include(":navigation:api")
include(":feature:list:api")
include(":feature:list:impl")
include(":feature:details:api")
include(":feature:details:impl")
include(":feature:login:api")
include(":feature:login:impl")
include(":feature:profile:api")
include(":feature:profile:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
