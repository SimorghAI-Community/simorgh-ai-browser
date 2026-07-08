// Top-level build file. Plugin versions are declared here and applied
// per-module with `apply false` at this level — actual application happens
// in each module's own build.gradle.kts.
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.24" apply false
}
