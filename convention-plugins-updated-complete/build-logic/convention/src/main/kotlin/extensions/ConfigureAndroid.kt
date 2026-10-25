package extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = 37
        defaultConfig.minSdk = 24

        if (this is ApplicationExtension) {
            defaultConfig.targetSdk = 37
        }
    }

    configureJavaAndroid(commonExtension)
    configureKotlin<KotlinAndroidProjectExtension>()
}
