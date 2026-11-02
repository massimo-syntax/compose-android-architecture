# Adopt MVI Pattern to ViewModel + Screen

Besides the `ViewModel`, an MVI contract is required.

- In the same package as the `ViewModel`, create a Kotlin file for the feature's MVI contract (e.g., `MyFeatureContract.kt`).
- In that file, define: **Event**, **State**, and **Effect** classes/interfaces.

---

## 1. MVI Contract (`MyFeatureContract.kt`)

### Events
**Events** represent user actions or UI triggers sent to the `ViewModel`.

```kotlin
sealed interface MyFeatureEvent {
    data object LoadFeatureData : MyFeatureEvent
    data class SomethingClicked(val model: MyModel) : MyFeatureEvent
}
```

### State
**State** represents the current UI state. Commonly `Loading`, `Success`, and `Error`.

```kotlin
sealed interface MyFeatureState {
    data object Loading : MyFeatureState

    data class Success(
        val data: List<MyModel>
    ) : MyFeatureState

    data class Error(
        val error: FailureClass
    ) : MyFeatureState
}
```

### Effect
**Effects** are one-off events transmitted by a `SharedFlow`. They are used for navigation, showing toasts/snackbars, or resolving user actions.

```kotlin
sealed interface MyFeatureEffect {
    data class NavigateToSomeScreen(val model: MyModel) : MyFeatureEffect
}
```

---

## 2. ViewModel Implementation (`MyFeatureViewModel.kt`)

To implement MVI, the `ViewModel` requires:

### UI State (Best Practice)
To trigger initial data loading automatically when the UI starts subscribing—avoiding logic in `init {}` or manual UI triggers—use `onStart` with `stateIn`:

```kotlin
private val _uiState = MutableStateFlow<MyFeatureState>(MyFeatureState.Loading)
val uiState = _uiState.asStateFlow()
    .onStart { event(MyFeatureEvent.LoadFeatureData) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MyFeatureState.Loading
    )
```

### Side Effects
Besides UI State, an Effect stream is used to emit single-shot actions:

```kotlin
private val _effect: MutableSharedFlow<MyFeatureEffect> = MutableSharedFlow(replay = 0)
val effect = _effect.asSharedFlow()
```

### Single Event Entry Point
The `ViewModel` processes all incoming UI events using a single handler function:

```kotlin
fun event(event: MyFeatureEvent) {
    when (event) {
        is MyFeatureEvent.SomethingClicked -> {
            viewModelScope.launch {
                _effect.emit(MyFeatureEffect.NavigateToSomeScreen(event.model))
            }
        }
        is MyFeatureEvent.LoadFeatureData -> {
            viewModelScope.launch {
                val result = repo.getData()
                when (result) {
                    is MyResult.Success -> {
                        _uiState.update { MyFeatureState.Success(result.data) }
                    }
                    is MyResult.Failure -> {
                        _uiState.update { MyFeatureState.Error(result.error) }
                    }
                }
            }
        }
    }
}
```

---

## 3. Composable Screen Integration (`MyFeatureScreen.kt`)

### Observing UI State & Side Effects
In the Composable, collect the state using lifecycle awareness and listen for effects in a `LaunchedEffect`:

```kotlin
@Composable
fun MyFeatureScreen(
    viewModel: MyFeatureViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Observe side effects (e.g., navigation)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MyFeatureEffect.NavigateToSomeScreen -> {
                    onNavigateToDetails(effect.model.id)
                }
            }
        }
    }

    // UI rendering based on uiState...
}
```

### Alternative: UI-driven Data Triggering (With Guard)
If data loading is triggered from the Composable instead of `onStart`, use a guard condition in a `LaunchedEffect` to avoid unnecessary re-fetching during back-navigation or recompositions:

> [!NOTE]
> `onStart` on `StateFlow` is the recommended best practice. Triggering data loading from the UI via `LaunchedEffect` is an alternative approach.

```kotlin
// Load items from UI only if not already loaded
LaunchedEffect(uiState) {
    if (uiState !is MyFeatureState.Success) {
        viewModel.event(MyFeatureEvent.LoadFeatureData)
    }
}
```

### Dispatching Events from UI
Send events to the `ViewModel` in response to user actions:

```kotlin
MessageItem(
    model = item,
    onItemClick = { selectedItem ->
        viewModel.event(MyFeatureEvent.SomethingClicked(selectedItem))
    }
)
```
