plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    // hilt
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    // serialization for retrofit (json)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.wallet"
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
        compose = true
        buildConfig = true
    }
}

dependencies {
    // modules
    implementation(projects.designsystem)
    implementation(projects.utils)
    implementation(projects.database)


    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // icons
    implementation(libs.androidx.compose.material.icons.extended)

    // coil
    implementation(libs.coil.compose)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    // json converter factory
    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation(libs.kotlinx.serialization.json)
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0")

    // okhttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:5.5.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

}