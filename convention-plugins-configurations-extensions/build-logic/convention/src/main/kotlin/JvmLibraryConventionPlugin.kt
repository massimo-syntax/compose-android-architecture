import com.android.build.api.dsl.LibraryExtension
import extensions.androidTestImplementation
import extensions.configureKotlinAndroid
import extensions.configureKotlinJvm
import extensions.debugImplementation
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import provider.libs



class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                // apply("org.jetbrains.kotlin.plugin.serialization")
            }
            extensions.configure<KotlinJvmProjectExtension> {
                configureKotlinJvm()
            }
            dependencies {
                    implementation(libs.androidx.core)
                    implementation(libs.androidx.lifecycle)
            }
        }
    }
}