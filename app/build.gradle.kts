plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.simorgh.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.simorgh.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Composition root — depends on all feature modules
    implementation(project(":feature:browser-core"))
    implementation(project(":feature:tabs-manager"))
    implementation(project(":feature:ai-agent"))
    implementation(project(":feature:persian-intelligence"))
    implementation(project(":feature:bookmarks"))
    implementation(project(":feature:downloads"))
    implementation(project(":feature:settings"))

    // Foundation modules needed to wire concrete implementations (DI graph)
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:ai-engine"))
    implementation(project(":core:memory"))
    implementation(project(":core:agent-runtime"))
}
