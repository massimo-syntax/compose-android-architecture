plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.data"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    // hlit
    implementation(libs.hilt)
    ksp(libs.hilt.compiler) // Use `ksp` instead of `kapt`
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    // retrofit
    implementation(libs.retrofit)

    implementation(project(":newmodule:domain"))

}