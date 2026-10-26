plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.applicationCompose)
}

android {
    namespace = "com.example.conventionpluginsupdatedcomplete"

    defaultConfig {
        applicationId = "com.example.conventionpluginsupdatedcomplete"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation(projects.core.domain)
    implementation(projects.myfeature)
}
