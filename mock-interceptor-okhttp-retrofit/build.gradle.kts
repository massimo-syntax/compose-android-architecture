// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // generate dagger hilt components
    id("com.google.dagger.hilt.android") version "2.59.2" apply false // example
    // WARNING , in the libs.versions file kotlin has to be upgraded
    // this was recommended from this ide as 2-10-1 or something..
    // changing here with 2.3.6 and kotlin in libs.toml to the newest recommended version of 2.3.21
    // did work without dsl error
    id("com.google.devtools.ksp") version "2.3.6" apply false // match Kotlin

}

