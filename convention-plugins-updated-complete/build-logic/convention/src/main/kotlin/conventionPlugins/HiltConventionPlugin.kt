package conventionPlugins

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the


class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.ksp.get().pluginId)
            apply(plugin = libs.plugins.daggerHilt.get().pluginId)

            dependencies {
                "implementation"(libs.androidx.activity.compose)
                "ksp"(libs.hilt.compiler)
                "implementation"(libs.hilt)
                "implementation"(libs.hilt.viewmodel)
            }
        }
    }
}