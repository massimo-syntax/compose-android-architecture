# Navigation 3 in a multi-module Compose project (Hilt) — reference & AI instructions

Use this file as a skill / AGENTS.md / prompt context. The rules in section 1 are
instructions for an AI; the rest is the reference implementation.

Replace `com.example` with the project's real package. Check latest stable versions
of all libraries; artifact names matter more than the versions shown here.

---

## 1. Rules for the AI (follow strictly)

1. Use **Navigation 3** (`androidx.navigation3`), not Navigation 2 / NavController.
2. If a `:navigation` module does not exist, **create it**. It is the only module that
   contains `NavDisplay` and `rememberNavBackStack`.
3. Every feature gets two submodules:
   - `:<feature>:api` → **only** `NavKey` classes (`@Serializable`). No Compose, no Hilt, no screens.
   - `:<feature>:impl` → screen(s), ViewModel(s), and **one entry extension function**
     on `EntryProviderScope<NavKey>` (Google's "entry builder").
4. `:impl` modules **never depend on other `:impl` modules**. An `:impl` may depend on
   another feature's `:api` (to get its key), but prefer taking a lambda
   (e.g. `onNavigateToMyFeature: (String) -> Unit`) so features stay decoupled.
5. `:navigation` depends on every feature's `:impl` (and `:api` where it builds a key).
   It wires the entries directly inside `entryProvider { ... }`. No DI for entries
   (keep it simple; see section 7 for when to change).
6. Screens receive navigation as **lambdas**, never a back stack or controller.
7. ViewModels use **Hilt** via `androidx.hilt:hilt-lifecycle-viewmodel-compose`
   (`hiltViewModel`). **Do NOT add `hilt-navigation-compose`.**
8. Arguments (e.g. an `id`) are passed to the ViewModel with **Hilt assisted injection**
   (`@HiltViewModel(assistedFactory = ...)`), created inside the entry using the key.
9. Always add `rememberSaveableStateHolderNavEntryDecorator()` and
   `rememberViewModelStoreNavEntryDecorator()` to `NavDisplay`, otherwise
   ViewModels are not scoped to their entry.
10. Keys must be `@Serializable` and implement `NavKey` so `rememberNavBackStack`
    survives configuration change and process death.
11. Do not create a ViewModel in `:navigation` just to hold the back stack.
    `rememberNavBackStack` is enough.
12. Adding a feature = create `:api` + `:impl`, add the dependency in `:navigation`,
    add one line in `entryProvider { ... }`. Nothing else changes.

---

## 2. Module graph

```
:feature:api        FeatureKey                                  (nav3-runtime, serialization)
:feature:impl       FeatureScreen + featureEntry()              -> :feature:api
:myfeature:api      MyFeatureKey(id)                            (nav3-runtime, serialization)
:myfeature:impl     MyFeatureScreen + VM + myFeatureEntry()     -> :myfeature:api
:navigation         AppNavigation (NavDisplay)                  -> all :api + all :impl
:app                MainActivity (@AndroidEntryPoint)           -> :navigation
                    Application (@HiltAndroidApp)
```

---

## 3. Gradle

### `:<feature>:api` (keys only)

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation("androidx.navigation3:navigation3-runtime:<ver>")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:<ver>")
}
```

### `:<feature>:impl` (screens, ViewModels, entry)

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":myfeature:api"))

    implementation("androidx.navigation3:navigation3-runtime:<ver>")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel-compose:<ver>") // hiltViewModel WITHOUT hilt-navigation-compose
    implementation("com.google.dagger:hilt-android:<ver>")
    ksp("com.google.dagger:hilt-compiler:<ver>")
    // + compose bom, material3, lifecycle-viewmodel-compose as needed
}
```

### `:navigation`

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

dependencies {
    api(project(":feature:api"))        // start key is used by the host
    implementation(project(":feature:impl"))
    implementation(project(":myfeature:api"))
    implementation(project(":myfeature:impl"))

    implementation("androidx.navigation3:navigation3-runtime:<ver>")
    implementation("androidx.navigation3:navigation3-ui:<ver>")
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:<ver>") // rememberViewModelStoreNavEntryDecorator
}
```

### `:app`

Hilt + KSP plugins, `@HiltAndroidApp` Application, depends on `:navigation`.
If a ViewModel is missing at runtime, verify the `:impl` module is reachable
from `:app` (directly or transitively) so Hilt aggregates it.

---

## 4. Code: `:api` modules

```kotlin
// :feature:api
package com.example.feature.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object FeatureKey : NavKey
```

```kotlin
// :myfeature:api
package com.example.myfeature.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class MyFeatureKey(val id: String) : NavKey
```

---

## 5. Code: `:impl` modules

### `:feature:impl`

```kotlin
package com.example.feature.impl

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.api.FeatureKey

fun EntryProviderScope<NavKey>.featureEntry(
    onNavigateToMyFeature: (id: String) -> Unit,
) {
    entry<FeatureKey> {
        FeatureScreen(onNavigateToMyFeature = onNavigateToMyFeature)
    }
}
```

```kotlin
@Composable
internal fun FeatureScreen(onNavigateToMyFeature: (id: String) -> Unit) {
    // Example: the screen decides which id to pass
    Button(onClick = { onNavigateToMyFeature("42") }) {
        Text("Open MyFeature")
    }
}
```

### `:myfeature:impl`

```kotlin
package com.example.myfeature.impl

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.myfeature.api.MyFeatureKey

fun EntryProviderScope<NavKey>.myFeatureEntry() {
    entry<MyFeatureKey> { key ->
        val viewModel = hiltViewModel<MyFeatureViewModel, MyFeatureViewModel.Factory>(
            creationCallback = { factory -> factory.create(key.id) }
        )
        MyFeatureScreen(viewModel)
    }
}
```

```kotlin
@HiltViewModel(assistedFactory = MyFeatureViewModel.Factory::class)
class MyFeatureViewModel @AssistedInject constructor(
    @Assisted private val id: String,
    private val repository: MyRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: String): MyFeatureViewModel
    }
}
```

```kotlin
@Composable
internal fun MyFeatureScreen(viewModel: MyFeatureViewModel) {
    // collect state from viewModel, render UI
}
```

Note: the `hiltViewModel` import path/signature for the non-`hilt-navigation-compose`
artifact can differ between versions. Verify against the version you use.

---

## 6. Code: `:navigation` and `:app`

```kotlin
package com.example.navigation

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(FeatureKey)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(), // scopes each ViewModel to its entry
        ),
        entryProvider = entryProvider {
            featureEntry(
                onNavigateToMyFeature = { id -> backStack.add(MyFeatureKey(id)) }
            )
            myFeatureEntry()
        },
    )
}
```

```kotlin
// :app
@HiltAndroidApp
class App : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme { AppNavigation() }
        }
    }
}
```

---

## 7. When to evolve (not needed up front)

- **Many features, or want to add one without touching `:navigation`:** switch to
  Hilt multibinding as in Google's guide. Each `:impl` provides its entry builder
  with `@IntoSet` (Google uses `EntryProviderScope<NavKey>.() -> Unit` in
  `ActivityRetainedComponent`), and the host installs the injected set. Inject the set
  in `MainActivity` with `@Inject` (use `@JvmSuppressWildcards`) and pass it to
  `AppNavigation`. Entry extensions can then become `internal`.
- **Bottom bar / multiple back stacks:** use a `NavigationState` + `Navigator`
  (see the nav3-recipes repo).
- **Start destination depends on login/onboarding:** use a small state holder that
  decides the start key; pass it to `AppNavigation(startKey = ...)`.

## 8. Official sources

- Modularize navigation code: https://developer.android.com/guide/navigation/navigation-3/modularize
- Navigation 3 recipes: https://github.com/android/nav3-recipes

## 9. Checklist: add a new feature

- [ ] `:newfeature:api` with `@Serializable` `NavKey`(s)
- [ ] `:newfeature:impl` with screen, `@HiltViewModel` (assisted if it takes args), and `newFeatureEntry()`
- [ ] Navigation out of the feature happens via lambdas passed to the entry
- [ ] `:navigation` gets the module dependencies and one line in `entryProvider { }`
- [ ] No dependency between `:impl` modules
