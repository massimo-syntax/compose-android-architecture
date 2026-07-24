import com.android.build.api.dsl.LibraryExtension
import extensions.androidTestImplementation
import extensions.configureKotlinAndroid
import extensions.debugImplementation
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import provider.libs

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                // Failed to apply plugin 'org.jetbrains.kotlin.android'
                // The 'org.jetbrains.kotlin.android' plugin is no longer required for Kotlin support since AGP 9.0.
                // apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                buildFeatures.compose = true
            }

            dependencies {
                implementation(platform(libs.androidx.compose.bom))
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.compose.ui)
                implementation(libs.androidx.compose.ui.graphics)
                implementation(libs.androidx.compose.ui.tooling.preview)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.runtime.ktx)
                testImplementation(libs.junit)
                androidTestImplementation(platform(libs.androidx.compose.bom))
                androidTestImplementation(libs.androidx.compose.ui.test.junit4)
                androidTestImplementation(libs.androidx.espresso.core)
                androidTestImplementation(libs.androidx.junit)
                debugImplementation(libs.androidx.compose.ui.test.manifest)
                debugImplementation(libs.androidx.compose.ui.tooling)
            }
        }
    }
}