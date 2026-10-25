# Convention Plugins Guide

> **Important:** Follow all instructions regarding the `build-logic` folder structure strictly in sequence, one step after another.

## 1. Initialize `build-logic` Project
- Create a new directory named `build-logic` in the root folder.
- Create a `settings.gradle.kts` file inside `build-logic/`.
- Add the following configuration to `build-logic/settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
```

## 2. Include `build-logic` in Root `settings.gradle.kts`
In the root project's `settings.gradle.kts`, include the composite build by adding `includeBuild("build-logic")` inside the `pluginManagement` block:

```kotlin
pluginManagement {
    includeBuild("build-logic")
    ...
}
```

## 3. Sync Gradle Project
- Perform a **Gradle Sync** in Android Studio to ensure the `build-logic` composite build is registered and recognized.

## 4. Include `:convention` Module
1. Create a new module directory named `convention` inside `build-logic/` (`build-logic/convention`).
2. Add the module inclusion at the end of `build-logic/settings.gradle.kts`:
   ```kotlin
   include(":convention")
   ```
3. Add the source folder structure to the `:convention` module:
   `src/main/kotlin`

## 5. Update Version Catalog (`gradle/libs.versions.toml`)
Add the `build-logic` dependencies and plugin definitions (`android-application`, `android-library`, `kotlin-jvm`, `kotlin-android`, `kotlin-compose`) to `gradle/libs.versions.toml`:

### In `[libraries]` section:
```toml
# Dependencies of the included build-logic
android-gradleApiPlugin = { group = "com.android.tools.build", name = "gradle-api", version.ref = "agp" }
compose-gradlePlugin = { module = "org.jetbrains.kotlin:compose-compiler-gradle-plugin", version.ref = "kotlin" }
kotlin-gradlePlugin = { group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version.ref = "kotlin" }
```

### In `[plugins]` section:
Add Android application, Android library, and Kotlin plugin definitions:
```toml
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

## 6. Declare Plugins in Root `build.gradle.kts`
In the root project's `build.gradle.kts`, declare all catalog plugins with `apply false` so they are available across the project:

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
```

## 7. Configure `build.gradle.kts` in `:convention` Module
Add the following configuration to `build-logic/convention/build.gradle.kts` to access the required Gradle plugin dependencies:

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.example.somename.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    // Add any other needed external plugin like: KSP, Hilt, etc.

    // To allow getting version catalog references in convention plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        // Here we will register our custom plugins
    }
}
```

## 8. Optimize Build Performance (`gradle.properties`)
To improve build performance across the entire project, add the following configuration options to the root `gradle.properties` file:

```properties
# Run independent tasks in parallel to speed up multi-module builds
org.gradle.parallel=true
# Reuse outputs from previous builds to avoid re-running tasks unnecessarily
org.gradle.caching=true
# Configure only the modules required for the current build, reducing configuration time
org.gradle.configureondemand=true
# Cache the result of the configuration phase so future builds can skip it entirely
org.gradle.configuration-cache=true
# Allow configuration caching work to run in parallel for even faster setup
org.gradle.configuration-cache.parallel=true
```

## 9. Create Packages in `:convention` Module
Inside `build-logic/convention/src/main/kotlin/`, create the following two packages:
- `extensions`
- `conventionPlugins`

## 10. Create Extension Helpers in `extensions` Package
Inside `build-logic/convention/src/main/kotlin/extensions/`, create the following extension files:

### 10.1 `ConfigureKotlin.kt`
```kotlin
package extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> throw Exception("Unsupported Kotlin project type")
    }.apply {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}
```

### 10.2 `ConfigureJava.kt`
*(Intended for pure Kotlin modules without Android dependencies—for example, domain-layer modules containing use cases, repository interfaces, and models)*

```kotlin
package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

private val javaVersion = JavaVersion.VERSION_17

internal fun Project.configureJavaAndroid(commonExtension: CommonExtension) {
   commonExtension.apply {
      compileOptions.sourceCompatibility = javaVersion
      compileOptions.targetCompatibility = javaVersion
   }
}

internal fun Project.configureJavaJvm() {
   extensions.configure<JavaPluginExtension> {
      sourceCompatibility = javaVersion
      targetCompatibility = javaVersion
   }
}
```

### 10.3 `ConfigureAndroid.kt`
*(Shared configuration for Android application and library modules)*

```kotlin
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
```

## Now is possible to create the convention plugins

#### Create Convention Plugins in `conventionPlugins` Package
Now that the infrastructure and extensions are ready, convention plugins can be created in `build-logic/convention/src/main/kotlin/conventionPlugins/`:

### Application Convention Plugin (`ApplicationConventionPlugin.kt`)
Create `ApplicationConventionPlugin.kt` inside `build-logic/convention/src/main/kotlin/conventionPlugins/`:

```kotlin
package conventionPlugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.configureAndroid
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.android.application.get().pluginId)
           // no longer needed, after agp 9. 
           //apply(plugin = libs.plugins.kotlin.android.get().pluginId)

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroid(extension)
        }
    }
}
```
And bind it in the :convention build.gradle.kts
- notice the path -> conventionPlugins.ApplicationConventionPlugin
   that is the pacage inside src/main/kotlin

```kotlin
gradlePlugin {
    plugins {
        // Here we will register our custom plugins
        // Android
        register("androidApplication") {
            id = libs.plugins.application.get().pluginId
            implementationClass = "conventionPlugins.ApplicationConventionPlugin"
        }
    }
}
```

