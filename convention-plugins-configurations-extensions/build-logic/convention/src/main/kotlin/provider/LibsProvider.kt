package provider

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.accessors.runtime.extensionOf
import org.gradle.kotlin.dsl.getByType

/**
 * Provides access to the version catalog for the project.
 */
val Project.libs
    get(): LibrariesForLibs = extensionOf(this, "libs") as LibrariesForLibs

/**
 * Gets the version of a library from the version catalog.
 */
fun Project.versionOf(name: String): Int = extensions.getByType<VersionCatalogsExtension>()
    .named("libs")
    .findVersion(name)
    .get()
    .toString()
    .toInt()