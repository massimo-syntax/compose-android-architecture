plugins {
    id("myProject.android.application")
}

android {
    namespace = "com.example.conventionpluginsconfigurationlibs"
}

dependencies {
    implementation(project(":feature:gradle-plugins-jurney"))
}