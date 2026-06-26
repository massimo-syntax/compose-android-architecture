plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // hilt
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.multimoduleretrofit"
    compileSdk = 37


    defaultConfig {
        applicationId = "com.example.multimoduleretrofit"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
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

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)

    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler) // Use `ksp` instead of `kapt`
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    // hilt needs retrofit of course
    implementation(libs.retrofit)
    implementation(libs.converter.gson)


    implementation(project(":newmodule:ui"))
    implementation(project(":newmodule:domain"))
    implementation(project(":newmodule:data"))

//implementation(project(":core:di"))


}