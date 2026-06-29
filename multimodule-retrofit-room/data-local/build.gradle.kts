plugins {
    alias(libs.plugins.android.library)
    // room annotation
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.data_local"
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
    // room bundle runtime + ktx
    api(libs.bundles.local.storage)
    ksp(libs.androidx.room.compiler)
}