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


## PART 2, navigation from-in-to Scaffold.
### Navigation 3 – Multi-module setup (Login → Scaffold with bottom bar → Fullscreen)

Instructions for a human or an AI to implement. Follow the module boundaries exactly.

## 1. Goal

- App starts on **Login** (mock, `loggedIn = false`). Pressing a button goes to **Main**.
- **Main** is a `Scaffold` with a bottom bar and 3 tabs: `ScaffoldRoute1`, `ScaffoldRoute2`, `ScaffoldRoute3`.
- Tabs **replace each other** (no history between tabs, no per-tab back stack).
- From a tab, a click opens `AnotherFullscreen(id)`, shown **full screen, covering the bottom bar**. Back returns to the same tab.
- `AnotherFullscreen` is a normal entry of the app-level NavDisplay, so any route can open it.

## 2. Core idea: two NavDisplays, two back stacks

| Level | Back stack contains | Shows |
|---|---|---|
| **Outer** (`AppNavDisplay`) | `Login`, `Main`, `AnotherFullscreen(id)` | Full-screen routes, no bottom bar |
| **Inner** (`MainScaffold`) | exactly one tab key | The tab content inside the Scaffold |

- The `Main` entry in the outer display hosts the Scaffold and the inner NavDisplay.
- Opening a full-screen route from a tab pushes to the **outer** stack, so it covers the Scaffold. The `Main` entry stays below it, so back returns to the same tab.
- Switching tabs = replacing the single key of the **inner** stack (`tabBackStack[0] = tab`), so it never grows. No navigator is needed there.

## 3. Modules and what belongs to each

### `:feature-login:api`
```kotlin
@Serializable data object Login : NavKey
```

### `:feature-login:impl`
```kotlin
fun EntryProviderScope<NavKey>.loginEntry(navigator: Navigator) {
    entry<Login> { LoginScreen(onLoginClick = { navigator.replaceAll(Main) }) }
}
```
`LoginScreen` takes plain lambdas only (previewable, no navigator inside).

### `:feature-route1:api`, `:feature-route2:api`, `:feature-route3:api`
```kotlin
@Serializable data object ScaffoldRoute1 : NavKey   // in route1:api
@Serializable data object ScaffoldRoute2 : NavKey   // in route2:api
@Serializable data object ScaffoldRoute3 : NavKey   // in route3:api
```

### `:feature-route1:impl` (same pattern for 2 and 3)
```kotlin
fun EntryProviderScope<NavKey>.scaffoldRoute1Entry(appNavigator: Navigator) {
    entry<ScaffoldRoute1> {
        ScaffoldRoute1Screen(onItemClick = { id -> appNavigator.navigate(AnotherFullscreen(id)) })
    }
}
```
Important: tab entries receive the **app (outer) navigator**, because anything fullscreen must go to the outer stack.

### `:feature-fullscreen:api`
```kotlin
@Serializable data class AnotherFullscreen(val id: String) : NavKey
```
Keys carry primitives only, never domain models.

### `:feature-fullscreen:impl`
```kotlin
fun EntryProviderScope<NavKey>.anotherFullscreenEntry(appNavigator: Navigator) {
    entry<AnotherFullscreen> { key ->
        AnotherFullscreenScreen(id = key.id, onBack = appNavigator::goBack)
    }
}
```
If the ViewModel needs the id, inject it with Hilt assisted injection (`hiltViewModel` with a creation callback), keeping the screen free of navigation types.

### `:navigation`
Depends on every `:api` and `:impl`. It owns `Main`, the `Navigator`, and both displays.

```kotlin
@Serializable data object Main : NavKey

class Navigator(private val backStack: NavBackStack) {
    fun navigate(key: NavKey) { backStack.add(key) }
    fun goBack() { backStack.removeLastOrNull() }
    fun replaceAll(key: NavKey) { backStack.clear(); backStack.add(key) }
}
```

Outer display:
```kotlin
@Composable
fun AppNavDisplay() {
    val backStack = rememberNavBackStack(Login)
    val appNavigator = remember(backStack) { Navigator(backStack) }

    NavDisplay(
        backStack = backStack,
        onBack = appNavigator::goBack,
        entryProvider = entryProvider {
            loginEntry(appNavigator)
            anotherFullscreenEntry(appNavigator)
            entry<Main> { MainScaffold(appNavigator) }
        }
    )
}
```

Inner display (bottom bar):
```kotlin
private val tabs = listOf(ScaffoldRoute1, ScaffoldRoute2, ScaffoldRoute3)

@Composable
fun MainScaffold(appNavigator: Navigator) {
    val tabBackStack = rememberNavBackStack(ScaffoldRoute1)
    val current = tabBackStack.last()

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = current == tab,
                        onClick = { tabBackStack[0] = tab },   // replace the single key
                        icon = { /* icon per tab */ },
                        label = { /* label per tab */ }
                    )
                }
            }
        }
    ) { padding ->
        NavDisplay(
            modifier = Modifier.padding(padding),
            backStack = tabBackStack,
            onBack = { /* single entry, never pops */ },
            entryProvider = entryProvider {
                scaffoldRoute1Entry(appNavigator)
                scaffoldRoute2Entry(appNavigator)
                scaffoldRoute3Entry(appNavigator)
            }
        )
    }
}
```

## 4. Rules (do not break)

1. Screens (composables) expose **plain lambdas** (`onItemClick`, `onBack`). Only **entries** touch the `Navigator`.
2. Keys live in `:api`, entries and screens in `:impl`, displays and `Main` in `:navigation`.
3. `:impl` modules never depend on other `:impl` modules. Cross-feature navigation uses keys from `:api`.
4. Only one navigator exists: `appNavigator` (outer stack). The inner tab stack has no navigator; the bottom bar swaps its single key directly. Tab entries get `appNavigator` for fullscreen routes.
5. The `Navigator` only wraps the stack that `rememberNavBackStack` owns. Do **not** make it a singleton with its own list (it would lose process-death restore).
6. Login: use `replaceAll(Main)` so back never returns to Login.
7. System back: the inner display has one entry, so back falls through to the outer one.

## 5. Two buttons switching two views inside one tab

Do **not** add another back stack. Use local state (segmented buttons) or a `HorizontalPager` if swiping is wanted. A pager is a fine choice.

```kotlin
enum class Section { A, B }
var section by rememberSaveable { mutableStateOf(Section.A) }
// when (section) { A -> ContentA(); B -> ContentB() }
```

Use a nested stack only if the sub-screens need their own history, args, deep links, or belong to different modules.

## 6. Out of scope for now

- Per-tab history (each tab with its own stack).
- Hilt multibinding of entries (`:navigation` calls entries directly).
- Real auth state (later: choose the start key from a ViewModel, `if (loggedIn) Main else Login`).

## 7. Checklist

- [ ] Login → Main clears the stack
- [ ] Tabs replace each other, stack size stays 1
- [ ] Tab click on an item opens `AnotherFullscreen(id)` without the bottom bar
- [ ] Back from `AnotherFullscreen` returns to the same tab
- [ ] Back from `Main` exits the app
- [ ] Builds with each `:impl` depending only on `:api` modules, never on another `:impl`

Note: the `rememberNavBackStack(key)` overload without serializers needs a recent Navigation 3 version. On older alphas, pass the configuration/serializers explicitly.
