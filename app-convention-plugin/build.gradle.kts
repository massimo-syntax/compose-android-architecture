// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // These plugins are now available to all modules in the project
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
}