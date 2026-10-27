plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.data"
}

dependencies {
    implementation(projects.core.domain)
}
