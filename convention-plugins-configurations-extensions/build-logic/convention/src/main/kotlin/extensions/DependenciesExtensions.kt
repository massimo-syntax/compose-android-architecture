package extensions


import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.accessors.dm.LibrariesForLibsInPluginsBlock
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

val COMPILE_VERSION = JavaVersion.VERSION_17

/**
 * Adds an implementation dependency to the project.
 */
fun DependencyHandlerScope.implementation(name: Provider<MinimalExternalModuleDependency>) {
    add("implementation", name)
}
// implementation(libs.android.compose.ui)
// Argument type mismatch:
// actual type is 'LibrariesForLibs.AndroidxComposeUiLibraryAccessors',
//            but 'Provider<MinimalExternalModuleDependency>' was expected.

// also other LibrariesForLibs throw errors so the best deal is an Any overload
fun DependencyHandlerScope.implementation(name: Any) {
    add("implementation", name)
}

/**
 * Adds a test implementation dependency to the project.
 */
fun DependencyHandlerScope.testImplementation(name: Provider<MinimalExternalModuleDependency>) {
    add("testImplementation", name)
}

/**
 * Adds an android test implementation dependency to the project.
 */
fun DependencyHandlerScope.androidTestImplementation(name: Provider<MinimalExternalModuleDependency>) {
    add("androidTestImplementation", name)
}

/**
 * Adds a debug implementation dependency to the project.
 */
fun DependencyHandlerScope.debugImplementation(name: Provider<MinimalExternalModuleDependency>) {
    add("debugImplementation", name)
}

// debugImplementation(libs.androidx.compose.ui.tooling)
// Argument type mismatch:
// actual type is 'LibrariesForLibs.AndroidxComposeUiToolingLibraryAccessors',
// but            'Provider<MinimalExternalModuleDependency>' was expected.
fun DependencyHandlerScope.debugImplementation(name: LibrariesForLibs.AndroidxComposeUiToolingLibraryAccessors) {
    add("debugImplementation", name)
}
