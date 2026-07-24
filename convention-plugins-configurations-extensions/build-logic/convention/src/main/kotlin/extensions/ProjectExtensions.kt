package extensions


import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import provider.versionOf
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
/**
 * Configures the Kotlin settings for the project.
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = versionOf("compileSdk")

        defaultConfig.apply {
            minSdk = versionOf("minSdk")
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions.apply {
            sourceCompatibility = COMPILE_VERSION
            targetCompatibility = COMPILE_VERSION
        }

        buildTypes.apply {
            getByName("debug"){
                enableUnitTestCoverage = true
            }
            getByName("release") {
                enableUnitTestCoverage = true
            }
        }

    }

    configureKotlin()
}

/**
 * Configures the Kotlin settings for JVM projects.
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = COMPILE_VERSION
        targetCompatibility = COMPILE_VERSION
    }
    configureKotlin()
}

/**
 * Configures common Kotlin compiler settings.
 */

// should not be needed anymore with gradle 9.
private fun Project.configureKotlin() {

    /*
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = COMPILE_VERSION.toString()
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }
     */
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.addAll(listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            ))
        }
    }


    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }




}