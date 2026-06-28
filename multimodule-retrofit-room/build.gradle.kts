// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    // hilt
    id("com.google.dagger.hilt.android") version "2.59.2" apply false
    // that need also ksp
    id("com.google.devtools.ksp") version "2.3.9" apply false
}