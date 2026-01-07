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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CulturalExplorer"
include(":app")
include(":core")
include(":feature")
include(":feature:auth")
include(":core:designsystem")
include(":feature:auth:auth-api")
include(":feature:auth:auth-impl")
include(":core:domain")
include(":feature:book:book-impl")
include(":feature:book:book-api")
include(":feature:book:book-api")
include(":core:network")
include(":core:util")
include(":feature:quote:quote-impl")
include(":feature:quote:quote-api")
