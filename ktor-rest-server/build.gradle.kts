
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.config.yaml)
    // core
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)

    // logs
    implementation(libs.logback.classic)

    // Content negotiation for JSON
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.contentNegotiation)

    // Status pages for error handling
    implementation("io.ktor:ktor-server-status-pages-jvm")

    // Authentication
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")



    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
