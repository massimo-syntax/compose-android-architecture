plugins {
    alias(libs.plugins.android.library)
    // room + hilt symmble processing
    alias(libs.plugins.ksp)
    // hilt
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.database"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }

        debug {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    //implementation(libs.androidx.appcompat)
    //implementation(libs.androidx.core.ktx)

    // room
    implementation("androidx.room3:room3-runtime:3.0.3")
    ksp("androidx.room3:room3-compiler:3.0.3")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

}