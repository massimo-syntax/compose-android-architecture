package extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = 37
        defaultConfig.minSdk = 24
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures.buildConfig = true

        if (this is ApplicationExtension) {
            defaultConfig.targetSdk = 37
        }
        if (this is LibraryExtension){
            // else
        }
    }

    configureJavaAndroid(commonExtension)
    configureKotlin<KotlinAndroidProjectExtension>()
}
