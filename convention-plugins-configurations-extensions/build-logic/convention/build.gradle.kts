import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
}

group = "com.myProject.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    // The line below allows us to access the libs from version catalog directly in the plugins
    // out of to be imported by peaces to then sync again or is higligted as error
    gradle.serviceOf<DependenciesAccessors>().classes.asFiles.forEach {
        compileOnly(files(it.absolutePath))
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "myProject.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "myProject.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "myProject.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}