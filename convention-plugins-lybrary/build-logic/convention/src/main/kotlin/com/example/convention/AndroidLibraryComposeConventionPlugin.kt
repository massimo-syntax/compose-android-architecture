package com.example.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("*** AndroidApplicationComposeConventionPlugin invoked ***")
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroidLibrary(this)
                // no targetSdk to declare in AndroidLibrary
            }
        }
    }
}

internal fun Project.configureKotlinAndroidLibrary (commonExtension: CommonExtension) {

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val getLib = { lib: String -> libs.findLibrary(lib).get() }

    commonExtension.apply {
        defaultConfig.minSdk = 24
        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17
        buildFeatures.compose = true
        compileSdk = 37

        dependencies {
            // kotlin
            add("implementation", getLib("androidx-core-ktx"))
            add("implementation", getLib("androidx-lifecycle-runtime-ktx"))

            // compose
            add("implementation", getLib("androidx-compose-ui"))
            add("implementation", getLib("androidx-compose-ui-graphics"))
            add("implementation", getLib("androidx-compose-ui-tooling"))
            add("implementation", getLib("androidx-compose-ui-tooling-preview"))
            add("implementation", getLib("androidx-compose-material3"))

            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }
    }
}
