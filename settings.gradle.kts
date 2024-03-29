pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "Nekome"
include(
    ":app",
    ":core:compose",
    ":core:preferences",
    ":core:resources",
    ":features:login",
    ":features:search",
    ":features:series",
    ":features:serieswidget",
    ":features:settings",
    ":libraries:core",
    ":libraries:database",
    ":libraries:datasource:activity",
    ":libraries:datasource:auth",
    ":libraries:datasource:search",
    ":libraries:datasource:series",
    ":libraries:datasource:trending",
    ":libraries:datasource:user",
    ":libraries:kitsu",
    ":libraries:kitsu:activity",
    ":libraries:kitsu:auth",
    ":libraries:kitsu:library",
    ":libraries:kitsu:search",
    ":libraries:kitsu:trending",
    ":libraries:kitsu:user",
    ":testing"
)
