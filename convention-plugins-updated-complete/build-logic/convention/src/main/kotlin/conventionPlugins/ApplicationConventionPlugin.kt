package conventionPlugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.configureAndroid
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.android.application.get().pluginId)
            //apply(plugin = libs.plugins.kotlin.android.get().pluginId)

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroid(extension)
        }
    }
}
