package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

internal fun Project.configureCompose(
    commonExtension: CommonExtension,
) {
    val libs = the<LibrariesForLibs>()

    commonExtension.apply {
        buildFeatures.compose = true

        dependencies {
            "implementation"(platform(libs.androidx.compose.bom))
            "implementation"(libs.androidx.compose.ui)
            "implementation"(libs.androidx.compose.ui.graphics)
            "implementation"(libs.androidx.compose.ui.tooling.preview)
            "implementation"(libs.androidx.compose.material3)
            "androidTestImplementation"(platform(libs.androidx.compose.bom))
            "androidTestImplementation"(libs.androidx.compose.ui.test.junit4)
            "debugImplementation"(libs.androidx.compose.ui.tooling)
            "debugImplementation"(libs.androidx.compose.ui.test.manifest)
        }
    }
}
