import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.example.somename.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
    //compileOnly(libs.kotlin.gradlePlugin) // not needed after agp 9.
    compileOnly(libs.compose.gradlePlugin)
    // Add any other needed external plugin like: KSP, Hilt, etc.
    compileOnly(libs.ksp.gradlePlugin)

    // To allow getting version catalog references in convention plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        // Here we will register our custom plugins
        // Android
        register("androidApplication") {
            id = libs.plugins.application.get().pluginId
            implementationClass = "conventionPlugins.ApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.library.get().pluginId
            implementationClass = "conventionPlugins.LibraryConventionPlugin"
        }
        register("jvmKotlin") {
            id = libs.plugins.convention.jvm.get().pluginId
            implementationClass = "conventionPlugins.JvmConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.applicationCompose.get().pluginId
            implementationClass = "conventionPlugins.ComposeApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.libraryCompose.get().pluginId
            implementationClass = "conventionPlugins.ComposeLibraryConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.hilt.get().pluginId
            implementationClass = "conventionPlugins.HiltConventionPlugin"
        }
    }
}
