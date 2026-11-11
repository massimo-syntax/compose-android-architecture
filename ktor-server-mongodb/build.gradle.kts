
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)

    // gson
    implementation(ktorLibs.serialization.gson)

    // koin
    implementation(libs.koin.ktor)
    implementation(libs.koin.loggerSlf4j)


    // MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.14.0")

    // Client
    val ktorClient = "3.6.1"
    implementation("io.ktor:ktor-client-core:$ktorClient")
    implementation("io.ktor:ktor-client-cio:$ktorClient")

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
