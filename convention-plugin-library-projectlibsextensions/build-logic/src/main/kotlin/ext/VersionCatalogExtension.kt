package ext

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

fun Project.versionCatalog(): VersionCatalog =
    extensions.getByType(VersionCatalogsExtension::class.java)
        .named("libs")

fun VersionCatalog.lib(alias: String) = findLibrary(alias).get()

fun VersionCatalog.bundle(alias: String) = findBundle(alias).get()

fun VersionCatalog.version(alias: String) = findVersion(alias).get().requiredVersion