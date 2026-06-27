plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    // hilt
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.ui"
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
    buildFeatures {
        compose = true
    }
}


dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose) // viewmodel
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // hiltViewModel()
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    implementation(project(":feature:todo:data"))
    implementation(project(":feature:todo:domain"))
}