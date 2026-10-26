plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.libraryCompose)
}

android {
    namespace = "com.example.myfeature"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}