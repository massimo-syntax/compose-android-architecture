plugins {
    // alias(libs.plugins.android.application)
    // alias(libs.plugins.kotlin.compose)
    id("com.buildlogicconventionsettingsgradle.convention.application.compose")
}

android {
    namespace = "com.example.conventionpluginlibrary"
}

dependencies{
    implementation(project(":ui"))
}