package conventionPlugins

import extensions.configureJavaJvm
import extensions.configureKotlin
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class JvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.kotlin.jvm.get().pluginId)

            configureJavaJvm()
            configureKotlin<KotlinJvmProjectExtension>()
        }
    }
}
