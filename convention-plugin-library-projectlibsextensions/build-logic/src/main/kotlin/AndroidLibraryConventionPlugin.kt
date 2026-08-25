import com.android.build.api.dsl.LibraryExtension
import ext.configureKotlin
import ext.version
import ext.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            // of course
            // and even more of course..
            // is that in version catalog [plugins]
            // android-library = { id = "com.android.library", version.ref = "agp" }
            pluginManager.apply("com.android.library")
            val libs = versionCatalog()

            extensions.configure(LibraryExtension::class.java){
                defaultConfig{
                    // thats directly copied from the default auto generated one
                    // of any, or maybe the last, module created, one that is not like the app module, for example
                    defaultConfig {
                        minSdk = libs.version("minSdk").toInt()
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                    compileSdk = libs.version("compileSdk").toInt()
                }
            }

            configureKotlin()

            dependencies {
                val IMPLEMENTATION = "implementation"
                add(IMPLEMENTATION, libs.findLibrary("androidx-core-ktx").get())
            }

        }
    }
}