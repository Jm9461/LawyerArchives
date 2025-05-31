// LawyerArchives/settings.gradle.kts
pluginManagement {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
        gradlePluginPortal() // Gradle Plugin Portal
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
    }
}
// Include the 'app' module in the project
include(":app")
