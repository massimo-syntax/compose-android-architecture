plugins {
    alias(libs.plugins.android.library)
    // hilt
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.data_remote"
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

    implementation(project(":domain"))

    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    // retrofit
    implementation(libs.retrofit)
    // moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    // also cool..
    // network dependencies (retrofit & okhttp)
    // implementation(libs.bundles.networking)
    // ksp(libs.moshi.codegen)

     //javax inject (enable to call annotation @Inject in kotlin)
     //implementation(libs.javax.inject)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)



}