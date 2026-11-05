# Deep links with Navigation 3 (multi-module, Compose, Hilt)

Instructions for adding deep link support to a Navigation 3 app. Follow the
module rules and the file layout exactly. Do not add multibinding, reflection,
or a navigation library on top of Nav3.

## 1. Core idea

- Nav3 has no deep link parser. The back stack is a list of `NavKey`s we own.
- A deep link is parsed **once** into a `List<NavKey>` (the full back stack).
  That list is the initial back stack: `[Main(...), FullscreenA, FullscreenB]`.
- `Main` is the scaffold with the bottom bar. It carries the selected tab (and
  an optional subtab) as plain serializable data, so the nested `NavDisplay`
  inside the scaffold can start on the right tab.
- Pressing back from a fullscreen screen returns to the same `Main` entry,
  which still shows the right tab.
- `DeepLinkPath` only parses. The saved state is the `NavKey`s, not
  `DeepLinkPath`, so `DeepLinkPath` does not need to be serializable.

Link format: `deeplink://<tab>/<rest...>`

| Link | Resulting back stack |
|---|---|
| `deeplink://tab1` | `[Main(TAB1)]` |
| `deeplink://tab1/detail/123` | `[Main(TAB1), ElementDetail(123)]` |
| `deeplink://tab2/preferences/color` | `[Main(TAB2), Preferences, ColorPreferences]` |
| `deeplink://tab3/activity` | `[Main(TAB3, "activity")]` |
| anything unknown, null, malformed | `[Main(TAB1)]` (safe default, never crash) |

## 2. Modules

```
:navigation              NavDisplay, Main, MainScaffold, DeepLinkPath, Navigator
:feature:<name>:api      NavKeys only (@Serializable), plus enums owned by that feature
:feature:<name>:impl     entry extension on EntryProviderScope, screen, ViewModel (Hilt)
```

Rules:

- `:api` modules never depend on each other. `:impl` modules never depend on
  each other.
- `:navigation` depends on every `:api` and `:impl` module. It is the only place
  that knows all keys. It calls each feature's entry function directly
  (no multibinding).
- A feature owns its own subtab type (enum) in its `:api`. `:navigation` does
  not know it. It only passes the raw `String?` segment through.
- Hilt: use `hilt-lifecycle-viewmodel-compose` (not `hilt-navigation-compose`).
  A ViewModel that needs the key argument (for example `id`) gets it injected
  from the key, as already set up in the project.

## 3. Keys

```kotlin
// :navigation
enum class MainTab(val segment: String) {
    TAB1("tab1"), TAB2("tab2"), TAB3("tab3")
}

@Serializable
data class Main(
    val tab: MainTab = MainTab.TAB1,
    val subTab: String? = null,   // raw uri segment, meaning depends on the tab
) : NavKey

// feature :api modules (one per feature)
@Serializable data object ScaffoldTab1 : NavKey
@Serializable data object ScaffoldTab2 : NavKey
@Serializable data class ScaffoldTab3(val subTab: Tab3SubTab = Tab3SubTab.INFO) : NavKey

@Serializable data class ElementDetail(val id: Int) : NavKey   // fullscreen, from tab1 list
@Serializable data object Preferences : NavKey                 // fullscreen
@Serializable data object ColorPreferences : NavKey            // fullscreen

// tab3 :api, the feature owns its subtab enum
enum class Tab3SubTab(val segment: String) {
    INFO("info"), ACTIVITY("activity");

    companion object {
        // unknown or null falls back to INFO, never throws
        fun from(segment: String?) = entries.firstOrNull { it.segment == segment } ?: INFO
    }
}
```

Why `Main` holds an enum plus a `String?` and not a `NavKey`: both are
serializable with no extra setup. A `NavKey` field would be polymorphic and
would need a registered `SerializersModule` for every key.

To add subtabs to another tab later: pass `rest.firstOrNull()` as `subTab` in
that tab's parse function and add an enum with a `from(segment)` in that tab's
`:api`.

## 4. DeepLinkPath

One private function per tab, all hardcoded and easy to read. To add a tab, add
an enum entry, a parse function, and one line in the `when`.

```kotlin
import java.net.URI

class DeepLinkPath(link: String?) {

    /** Full back stack. Always starts with Main. */
    val stack: List<NavKey>

    init {
        val segments = parseSegments(link)   // e.g. ["tab1", "detail", "123"]
        val rest = segments.drop(1)          // everything after the tab

        stack = when (segments.firstOrNull()) {
            "tab1" -> parseScaffold1(rest)
            "tab2" -> parseScreenChain(rest)
            "tab3" -> parseScaffold3(rest)
            else   -> listOf(Main(MainTab.TAB1))   // unknown or empty: safe default
        }
    }

    // tab1 has a list; one fullscreen screen with an id argument.
    // tab1/detail/123 -> [Main(TAB1), ElementDetail(123)]
    private fun parseScaffold1(rest: List<String>): List<NavKey> {
        val main = Main(MainTab.TAB1)
        val id = rest.getOrNull(1)?.toIntOrNull()
        return if (rest.firstOrNull() == "detail" && id != null) {
            listOf(main, ElementDetail(id))
        } else {
            listOf(main)
        }
    }

    // Parses fullscreen screens one after another: each valid segment adds one
    // screen on top of the previous one. Stops at the first unknown segment, so
    // a bad link degrades to a shorter stack instead of crashing.
    // Useful for any chain like a/b/c -> [Main, A, B, C].
    // tab2/preferences/color -> [Main(TAB2), Preferences, ColorPreferences]
    private fun parseScreenChain(rest: List<String>): List<NavKey> {
        val stack = mutableListOf<NavKey>(Main(MainTab.TAB2))
        if (rest.getOrNull(0) != "preferences") return stack
        stack += Preferences
        if (rest.getOrNull(1) == "color") stack += ColorPreferences
        return stack
    }

    // tab3 has no fullscreen screens, only a subtab.
    // Or build every other kind of data structure from the uri segments here
    // (ids, filters, query-like values) and put it in Main or in a fullscreen key.
    // tab3/activity -> [Main(TAB3, "activity")]
    private fun parseScaffold3(rest: List<String>): List<NavKey> =
        listOf(Main(MainTab.TAB3, subTab = rest.firstOrNull()))

    private fun parseSegments(link: String?): List<String> {
        val uri = runCatching { URI(link ?: return emptyList()) }.getOrNull()
            ?: return emptyList()
        if (!uri.scheme.equals("deeplink", ignoreCase = true)) return emptyList()
        // deeplink://tab2/preferences/color -> host = tab2, path = /preferences/color
        val path = uri.path.orEmpty().split("/").filter { it.isNotBlank() }
        return (listOfNotNull(uri.host) + path).map { it.lowercase() }
    }
}
```

Notes:

- `java.net.URI` keeps the class pure Kotlin, unit-testable without Robolectric.
- Host names must use letters, digits, and hyphens. Underscores make `uri.host`
  return `null`.
- The parser never validates feature subtabs. It passes the raw string through
  and the owning feature falls back to a default.
- Every parse function returns a valid chain or stops at the last valid step.
  Nothing throws.

## 5. Passing the arguments to the NavDisplay

Outer stack, in the Activity:

```kotlin
val path = DeepLinkPath(intent?.data?.toString())
val backStack = rememberNavBackStack(*path.stack.toTypedArray())
```

`rememberNavBackStack(...)` uses the initial elements only when nothing was
restored. After rotation or process death the saved stack wins, so the link is
consumed once and never re-applied.

When the app is already running, in `onNewIntent`:

```kotlin
backStack.clear()
backStack.addAll(DeepLinkPath(intent.data?.toString()).stack)
```

The `Main` entry hands tab and subtab to the scaffold:

```kotlin
entry<Main> { key -> MainScaffold(key.tab, key.subTab) }
```

The scaffold owns a small inner back stack (the nested `NavDisplay`). Tabs
replace each other, with no history per tab. Tab switching is
`clear()` then `add(...)`.

```kotlin
@Composable
fun MainScaffold(initialTab: MainTab, initialSubTab: String?) {
    val tabStack = rememberNavBackStack(initialTab.toKey(initialSubTab))
    val current = tabStack.last()

    Scaffold(bottomBar = {
        BottomBar(
            selected = current.toTab(),
            onSelect = { tab ->
                if (tab != current.toTab()) { tabStack.clear(); tabStack.add(tab.toKey(null)) }
            },
        )
    }) { padding ->
        NavDisplay(
            backStack = tabStack,
            modifier = Modifier.padding(padding),
            entryProvider = entryProvider {
                entry<ScaffoldTab1> { Tab1Screen() }        // contains the list
                entry<ScaffoldTab2> { Tab2Screen() }
                entry<ScaffoldTab3> { Tab3Screen(it.subTab) }
            },
        )
    }
}

// the only places that know every tab, both live in :navigation
private fun MainTab.toKey(subTab: String?): NavKey = when (this) {
    MainTab.TAB1 -> ScaffoldTab1
    MainTab.TAB2 -> ScaffoldTab2
    MainTab.TAB3 -> ScaffoldTab3(Tab3SubTab.from(subTab))
}

private fun NavKey.toTab(): MainTab = when (this) {
    is ScaffoldTab2 -> MainTab.TAB2
    is ScaffoldTab3 -> MainTab.TAB3
    else            -> MainTab.TAB1
}
```

Fullscreen screens (`ElementDetail`, `Preferences`, ...) are entries of the
**outer** `NavDisplay` in `:navigation`, so they cover the bottom bar. Each is
registered by calling the feature's entry extension:

```kotlin
entryProvider {
    entry<Main> { MainScaffold(it.tab, it.subTab) }
    elementDetailEntry()       // from the feature :impl
    preferencesEntry()
    colorPreferencesEntry()
}
```

Back from a fullscreen screen pops to `Main`. `MainScaffold` keeps its state,
so the user sees the tab they came from.

## 6. Serialization

- All keys are `@Serializable`. Enums and data classes of enums need no extra
  setup.
- Because `Main` holds only an enum and a `String?` (no `NavKey` field), no
  polymorphic `SerializersModule` is needed. Plain `rememberNavBackStack(...)`
  works for both stacks.
- If a key ever holds a `NavKey` or an interface-typed field, register that base
  type with `polymorphic(...)` in a `SavedStateConfiguration` and pass it to
  `rememberNavBackStack(config, ...)`.

## 7. Manifest

```xml
<activity ...>
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="deeplink" />
    </intent-filter>
</activity>
```

## 8. Login gating

Flow: Login (mock) -> Main -> fullscreen screens.

If the user is not logged in, do not build the stack from the link directly.
Keep the `DeepLinkPath` as pending, start at Login, and after login succeeds:

```kotlin
backStack.clear()
backStack.addAll(pending.stack)
```

## 9. Checklist for adding a new deep link

1. New fullscreen screen: add its key to the feature's `:api`, its entry
   extension to `:impl`, and register the entry in `:navigation`.
2. Add the segment handling to the right `parse...` function in `DeepLinkPath`.
3. New tab: add a `MainTab` entry, the tab key in its feature `:api`, a
   `parseX` function, one line in the `when`, plus `toKey` / `toTab` and an
   `entry<>` in `MainScaffold`.
4. Add a unit test for `DeepLinkPath` with valid, partial, and invalid links.
5. Test state restoration with "Don't keep activities" enabled.
