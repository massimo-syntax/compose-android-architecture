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
    //compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    // Add any other needed external plugin like: KSP, Hilt, etc.

    // To allow getting version catalog references in convention plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    // if that does not work:
    //gradle.serviceOf<DependenciesAccessors>().classes.asFiles.forEach {
    //    compileOnly(files(it.absolutePath))
    //}

}

gradlePlugin {
    plugins {
        // Here we will register our custom plugins
        // Android
        register("androidApplication") {
            id = libs.plugins.application.get().pluginId
            implementationClass = "conventionPlugins.ApplicationConventionPlugin"
        }
    }
}
