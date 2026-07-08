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
    }
}

rootProject.name = "simorgh-ai-browser"

include(":app")

// core:* — Foundation layer (ARCHITECTURE.md section 5)
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":core:ai-engine")
include(":core:memory")
include(":core:agent-runtime")

// feature:* — Browser Core + Intelligence + UI layers
include(":feature:browser-core")
include(":feature:tabs-manager")
include(":feature:ai-agent")
include(":feature:persian-intelligence")
include(":feature:bookmarks")
include(":feature:downloads")
include(":feature:settings")
