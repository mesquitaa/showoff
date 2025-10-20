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

rootProject.name = "ShowOff"
include(":app")

// Core modules
include(":core:core-common")
include(":core:core-network")
include(":core:core-ui")

// Domain module
include(":domain:meal-domain")

// Features module
include(":feature:category-list")
