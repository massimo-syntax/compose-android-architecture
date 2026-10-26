package conventionPlugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.configureCompose
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

class ComposeApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.kotlin.compose.get().pluginId)

            val extension = extensions.getByType<ApplicationExtension>()
            configureCompose(extension)

            dependencies {
                "implementation"(libs.androidx.activity.compose)
            }
        }
    }
}
