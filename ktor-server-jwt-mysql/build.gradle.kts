
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

    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.config.yaml)
    implementation(libs.logback.classic)
    // koin
    implementation("io.insert-koin:koin-ktor:4.2.2")
    implementation("io.insert-koin:koin-logger-slf4j:4.2.2")

    // serialization
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.contentNegotiation)
    // jwt auth
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)

    // exposed sql connections
    implementation(libs.exposed.core)
    implementation(libs.exposed.r2dbc)
    // dao
    // https://www.jetbrains.com/help/exposed/migrating-from-jdbc-to-r2dbc-dao.html#update-dependencies
    //implementation("org.jetbrains.exposed:exposed-core:1.6.1")
    //implementation("org.jetbrains.exposed:exposed-r2dbc:1.6.1")
    implementation("org.jetbrains.exposed:exposed-dao-r2dbc:1.6.1")

    implementation(libs.h2database.h2)
    implementation(libs.h2database.r2dbc)

    // mysql
    implementation("io.asyncer:r2dbc-mysql:1.3.2")

    // encryption
    implementation("org.mindrot:jbcrypt:0.4")


    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
