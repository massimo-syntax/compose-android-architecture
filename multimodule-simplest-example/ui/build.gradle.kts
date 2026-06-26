// Source - https://stackoverflow.com/a/79263065
// Posted by easyScript
// Retrieved 2026-06-24, License - CC BY-SA 4.0
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}


android {
    namespace = "com.example.ui"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Source - https://stackoverflow.com/a/79263065
    // Posted by easyScript
    // Retrieved 2026-06-24, License - CC BY-SA 4.0
    buildFeatures {
        compose = true
    }


}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)


}