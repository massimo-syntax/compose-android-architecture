package ext

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension

fun Project.configureKotlin() {
    extensions.configure(KotlinAndroidExtension::class.java){
        jvmToolchain(17)
    }
}