plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.libraryCompose)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.myfeature"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(projects.core.domain)
}
