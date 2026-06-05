package com.example.hiltretrofit.data.remote

object MockServer{
    fun routeResponse(parameter:String): String {
        return when(parameter){
            RequestParam.ANDROID.formattedName -> androidFeatures
            RequestParam.KOTLIN.formattedName -> kotlinLibraries
            RequestParam.COMPOSE.formattedName-> composeFeatures
            else -> """{"id":"404","content":" $parameter not found"},"""
        }
    }
}


val kotlinLibraries = """
    [{"id":"c1f8b7a2","content":"Kotlinx.coroutines — structured concurrency, flows, and suspending functions for asynchronous programming."},{"id":"a4e3d9f0","content":"Kotlinx.serialization — multiplatform JSON, ProtoBuf, CBOR serializers with Kotlin-native support."},{"id":"9b2f6c5e","content":"Ktor — asynchronous framework for building servers and clients with coroutines and HTTP DSL."},{"id":"d7a1e4b3","content":"Exposed — lightweight SQL library and DSL for database access with type-safe queries."},{"id":"f0c9a8d7","content":"Kodein/ Koin — lightweight dependency injection libraries designed for Kotlin multiplatform."},{"id":"e5b2c3a1","content":"Arrow — functional programming library providing optics, typeclasses, and data types like Either and Option."},{"id":"b6d4f2c8","content":"Anko (deprecated but notable) — Kotlin-friendly Android utilities and DSLs for layouts and SQLite."},{"id":"8a7e1b4f","content":"MockK — mocking library tailored for Kotlin with coroutine and extension function support."},{"id":"3c9d2e6b","content":"Detekt — static code analysis tool for Kotlin with configurable rules and complexity reporting."},{"id":"0f5a8c3d","content":"Kotlinx.html — DSL for building HTML in Kotlin, useful for server-side templating."}]
""".trimIndent()

val composeFeatures = """
[{"id":"7f3b2a9c","content":"Declarative UI — build interfaces by describing UI state and layouts rather than imperative view updates."},{"id":"c2d9e1b4","content":"Composable functions — reusable UI building blocks annotated with @Composable for composition and recomposition."},{"id":"a9f6b8d2","content":"Recomposition — automatic UI updates when observable state changes, minimizing manual UI refresh."},{"id":"e4b7c3f1","content":"State management — remember, mutableStateOf, and derivedStateOf for managing and observing UI state."},{"id":"5d8c1e6a","content":"Material Design components — built-in Material 2 and Material 3 components and theming support."},{"id":"9a1e4c7f","content":"Layout system — flexible Row, Column, Box and custom layout composables with measurement and placement."},{"id":"b3f0d6a5","content":"Animation APIs — high-level animation libraries (animate*AsState, AnimatedVisibility) and low-level animation control."},{"id":"d6a2e9b0","content":"Interop with Views — integration with existing Android Views via AndroidView and ComposeView."},{"id":"8b5f3c2d","content":"Performance tooling — Compose Compiler optimizations, tooling for inspection, and LiveEdit support."},{"id":"f1c7a4e8","content":"Navigation — Compose Navigation library for declarative in-app navigation and backstack management."}]
""".trimIndent()

val androidFeatures = """[{"id":"c8a1d4f2","content":"Activities and Fragments — lifecycle-aware components for UI screens and modular UI pieces."},{"id":"a3b9e6c7","content":"Intents and PendingIntents — inter-component communication and deferred actions for notifications and alarms."},{"id":"f2d7b1a9","content":"Services — background processing with foreground services for ongoing tasks and bound services for IPC."},{"id":"e6c4a3b8","content":"Broadcast Receivers — listen for system and app broadcasts (use modern alternatives for implicit broadcasts)."},{"id":"b1f9c2d3","content":"Content Providers — share structured data across apps with URIs and permissions."},{"id":"9d7e5a4b","content":"View System — traditional XML layouts and view hierarchy with adapters and custom views."},{"id":"8f3b2c6e","content":"Notifications — channels, styles, and actionable notifications with proper priority and grouping."},{"id":"d4a8e7b5","content":"Permissions — runtime permission model with request/response flow and scoped storage considerations."},{"id":"7b2c9f1d","content":"Location and Sensors — fused location provider, GPS, and sensor framework for device data."},{"id":"5e6a3b8c","content":"Security and Authentication — KeyStore, biometric authentication, encrypted storage, and OAuth support."}]
""".trimMargin()

enum class RequestParam(val formattedName:String){
    ANDROID("android"),
    KOTLIN("kotlin"),
    COMPOSE("compose")
}
val choices = """
    [
    {"id":"0","content":"${RequestParam.COMPOSE.formattedName}"},
    {"id":"1","content":"${RequestParam.ANDROID.formattedName}"},
    {"id":"2","content":"${RequestParam.KOTLIN.formattedName}"}
    ]
""".trimIndent()