import com.android.build.api.dsl.ApplicationExtension
import extensions.androidTestImplementation
import extensions.configureKotlinAndroid
import extensions.debugImplementation
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import provider.libs
import org.gradle.kotlin.dsl.dependencies
import kotlin.jvm.kotlin

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.compose")
                // apply("org.jetbrains.kotlin.plugin.serialization")
                // apply("org.jetbrains.kotlin.android")
                // not needed anymore
            }

            extensions.configure<ApplicationExtension>{
                configureKotlinAndroid(this)

                defaultConfig{
                    targetSdk = libs.versions.targetSdk.get().toInt()
                }

                buildTypes {
                    release {
                        optimization {
                            enable = false
                        }
                    }
                }

            }

            dependencies{
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.runtime.ktx)

                implementation(platform(libs.androidx.compose.bom))
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.compose.ui)

                // theme
                implementation(libs.androidx.compose.material3)

            }
        }
    }
}