plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // hilt
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.multimodularmodernarchitecturefullapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.multimodularmodernarchitecturefullapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
        }

        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // modules
    implementation(projects.utils)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    // core + lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // hilt viewmodel, used also for navigation
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    // material icons
    implementation(libs.androidx.compose.material.icons.extended)

    // coil
    implementation(libs.coil.compose)

    // splash screen
    implementation(libs.androidx.core.splashscreen)


}
