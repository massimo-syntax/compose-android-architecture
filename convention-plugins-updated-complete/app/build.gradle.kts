plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.applicationCompose)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.conventionpluginsupdatedcomplete"

    defaultConfig {
        applicationId = "com.example.conventionpluginsupdatedcomplete"
        versionCode = 1
        versionName = "1.0"
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

    // modules
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.myfeature)
}
