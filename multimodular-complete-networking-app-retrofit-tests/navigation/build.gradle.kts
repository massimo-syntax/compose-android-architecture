plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.navigation"
    compileSdk {
        version = release(libs.versions.compileSkd.get().toInt()) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":navigation:api"))
    implementation(project(":core"))
    api(project(":feature:list:api"))
    implementation(project(":feature:list:impl"))
    implementation(project(":feature:details:api"))
    implementation(project(":feature:details:impl"))
    api(project(":feature:login:api"))
    implementation(project(":feature:login:impl"))
    api(project(":feature:profile:api"))
    implementation(project(":feature:profile:impl"))
    api(project(":feature:settings:api"))
    implementation(project(":feature:settings:impl"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
}
