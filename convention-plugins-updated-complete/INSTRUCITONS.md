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
        jvmToolchain(17)
        jvmTarget.set(JvmTarget.JVM_17)
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

## Now it is possible to create the convention plugins

---

-**Useful info: applying**
```kotlin
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

in the project root settings.gradle.kts, enables to access the modules by type safe decalration instead of string
```kotlin
    implementation(project.core.domain)
    implementation(project.myfeature)
    //  instead of
    implementation(project(":core:domain"))
    implementation(project(":myfeature"))
```

---


#### Create Convention Plugins in `conventionPlugins` Package

Now that the infrastructure and extensions are ready, convention plugins can be created in `build-logic/convention/src/main/kotlin/conventionPlugins/`:

## Application Convention Plugin (`ApplicationConventionPlugin.kt`)
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
            // No longer needed after AGP 9:
            // apply(plugin = libs.plugins.kotlin.android.get().pluginId)

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroid(extension)
        }
    }
}
```

And bind it in `build-logic/convention/build.gradle.kts`:
- Notice the implementation class path: `conventionPlugins.ApplicationConventionPlugin` (this corresponds to the package inside `src/main/kotlin`).

In the `gradle/libs.versions.toml` file, add the plugin definition under `[plugins]`:

```toml
[plugins]
# ...
application = { id = "convention.plugin.application" }
```

Register the plugin in `build-logic/convention/build.gradle.kts`:

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

### Apply Convention Plugin in `:app` Module (`app/build.gradle.kts`)

Now the convention plugin can replace the default Android application plugin in `app/build.gradle.kts`:

```kotlin
plugins {
    // Replace default plugin:
    // alias(libs.plugins.android.application)
    alias(libs.plugins.application) // our plugin
    ...
}
```

By applying the convention plugin, redundant configurations in the `:app` module can be removed because the convention plugin already configures:
- Java compile options via `configureJavaAndroid`:
  ```kotlin
  commonExtension.apply {
      compileOptions.sourceCompatibility = javaVersion
      compileOptions.targetCompatibility = javaVersion
  }
  ```
- Default SDK configurations via `configureAndroid`:
  ```kotlin
  commonExtension.apply {
      compileSdk = 37
      defaultConfig.minSdk = 24
      ...
      defaultConfig.targetSdk = 37
      
  }
  ```

With the new plugin applied, those configurations can be removed from the `android {}` block in `app/build.gradle.kts`:

```kotlin
android {
    namespace = "com.example.conventionpluginsupdatedcomplete"

    defaultConfig {
        applicationId = "com.example.conventionpluginsupdatedcomplete"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        ...
    }

    buildFeatures {
        compose = true
    }
}
```
---
## Library Convention Plugin (`LibraryConventionPlugin.kt`)
*(Used for Android library modules)*

### Step 1: Create `LibraryConventionPlugin.kt`
Create `LibraryConventionPlugin.kt` inside `build-logic/convention/src/main/kotlin/conventionPlugins/`:

```kotlin
package conventionPlugins

import com.android.build.api.dsl.LibraryExtension
import extensions.configureAndroid
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.android.library.get().pluginId)
            // No longer needed after AGP 9:
            // apply(plugin = libs.plugins.kotlin.android.get().pluginId)

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroid(extension)
        }
    }
}
```

### Step 2: Define Plugin ID in Version Catalog (`gradle/libs.versions.toml`)
Add the library convention plugin under `[plugins]`:

```toml
[plugins]
# ...
library = { id = "convetion.plugin.library" }
```

### Step 3: Register Plugin in `:convention` (`build-logic/convention/build.gradle.kts`)
Register the plugin inside the `gradlePlugin` block

```kotlin
gradlePlugin {
    plugins {
        // ...
        register("androidLibrary") {
            id = libs.plugins.library.get().pluginId
            implementationClass = "conventionPlugins.LibraryConventionPlugin"
        }
    }
}
```

### Step 4: Apply in Library Modules
In any library module's `build.gradle.kts`, apply the custom convention plugin in place of the default Android library plugin:
All defined configurations in our plugin can be removed from the gradle file
```kotlin
plugins {
    alias(libs.plugins.android.library) // remove
    alias(libs.plugins.library)
}
// remove entire block
compileSdk {
    version = release(37)
}

defaultConfig {
    minSdk = 24 // remove

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

// remove entire
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

```


---

## Pure Kotlin Convention Plugin (`JvmConventionPlugin.kt`)
*(Used by pure JVM/Kotlin modules—for example, domain modules containing use cases, repository interfaces, and models)*

### Step 1: Create `JvmConventionPlugin.kt`
Create `JvmConventionPlugin.kt` inside `build-logic/convention/src/main/kotlin/conventionPlugins/`:

```kotlin
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
```

### Step 2: Define Plugin ID in Version Catalog (`gradle/libs.versions.toml`)
Add the pure Kotlin convention plugin under `[plugins]`:

```toml
[plugins]
# ...
convention-jvm = { id = "convetion.plugin.kotlin.jvm" }
```

### Step 3: Register Plugin in `:convention` (`build-logic/convention/build.gradle.kts`)
Register the plugin inside the `gradlePlugin` block:

```kotlin
gradlePlugin {
    plugins {
        // ...
        register("jvmKotlin") {
            id = libs.plugins.convention.jvm.get().pluginId
            implementationClass = "conventionPlugins.JvmConventionPlugin"
        }
    }
}
```

### Step 4: Apply in Pure Kotlin Modules
In any pure Kotlin module's `build.gradle.kts`, apply the custom convention plugin in place of `kotlin("jvm")`:
In the convention plugin is already defined what can be deleted from the module gradle file
```kotlin
plugins {
    id("java-library") // remove
    alias(libs.plugins.kotlin.jvm) // remove
    alias(libs.plugins.convention.jvm) // our convention plugin
}
// remove whole
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
// remove as well the whole
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
```
---

## Configuration of Compose in Convention Plugins

### Common Compose Extension (`ConfigureCompose.kt`)
The common extension helper can be used by convention plugins to configure Jetpack Compose dependencies and build features.

Create `ConfigureCompose.kt` inside `build-logic/convention/src/main/kotlin/extensions/`:

```kotlin
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
```

### Application Compose Convention Plugin (`ComposeApplicationConventionPlugin.kt`)
*(Used for Android application modules requiring Jetpack Compose)*

#### Step 1: Create `ComposeApplicationConventionPlugin.kt`
Create `ComposeApplicationConventionPlugin.kt` inside `build-logic/convention/src/main/kotlin/conventionPlugins/`:

```kotlin
package conventionPlugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.configureCompose
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

class ComposeApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.kotlin.compose.get().pluginId)

            val extension = extensions.getByType<ApplicationExtension>()
            configureCompose(extension)

            dependencies {
                "implementation"(libs.androidx.activity.compose)
            }
        }
    }
}
```

#### Step 2: Define Plugin ID in Version Catalog (`gradle/libs.versions.toml`)
Add the application compose convention plugin under `[plugins]`:

```toml
[plugins]
# ...
applicationCompose = { id = "convetion.plugin.application.compose" }
```

#### Step 3: Register Plugin in `:convention` (`build-logic/convention/build.gradle.kts`)
Register the plugin inside the `gradlePlugin` block:

```kotlin
gradlePlugin {
    plugins {
        // ...
        register("androidApplicationCompose") {
            id = libs.plugins.applicationCompose.get().pluginId
            implementationClass = "conventionPlugins.ComposeApplicationConventionPlugin"
        }
    }
}
```

#### Step 4: Apply in Application Module (`app/build.gradle.kts`)
In `app/build.gradle.kts`, apply the custom application compose plugin alongside the base application plugin:

```kotlin
plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.applicationCompose)
}
```

All Compose dependencies provided by the convention plugin and its extension, as well as the build feature for compose `buildFeatures { compose = true }`, can now be safely removed from `app/build.gradle.kts` (or any application module).

---

### Library Compose Convention Plugin (`ComposeLibraryConventionPlugin.kt`)
*(Used for Android library modules requiring Jetpack Compose)*

#### Step 1: Create `ComposeLibraryConventionPlugin.kt`
Create `ComposeLibraryConventionPlugin.kt` inside `build-logic/convention/src/main/kotlin/conventionPlugins/`:

```kotlin
package conventionPlugins

import com.android.build.api.dsl.LibraryExtension
import extensions.configureCompose
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = the<LibrariesForLibs>()
            apply(plugin = libs.plugins.kotlin.compose.get().pluginId)

            val extension = extensions.getByType<LibraryExtension>()
            configureCompose(extension)
        }
    }
}
```

#### Step 2: Define Plugin ID in Version Catalog (`gradle/libs.versions.toml`)
Add the library compose convention plugin under `[plugins]`:

```toml
[plugins]
# ...
libraryCompose = { id = "convetion.plugin.library.compose" }
```

#### Step 3: Register Plugin in `:convention` (`build-logic/convention/build.gradle.kts`)
Register the plugin inside the `gradlePlugin` block:

```kotlin
gradlePlugin {
    plugins {
        // ...
        register("androidLibraryCompose") {
            id = libs.plugins.libraryCompose.get().pluginId
            implementationClass = "conventionPlugins.ComposeLibraryConventionPlugin"
        }
    }
}
```

#### Step 4: Apply in Library Modules (`build.gradle.kts`)
In any library module requiring Compose, apply the custom library compose plugin alongside the base library plugin:

```kotlin
plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.libraryCompose)
}
```
Now everything about compose, all the libraries and build feature, are not needed in the module

---

