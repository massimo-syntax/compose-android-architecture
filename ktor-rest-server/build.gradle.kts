
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

// docker
ktor {
    docker {
        // ...
        jreVersion.set(JavaVersion.VERSION_21) // same as toolchain
        localImageName.set("sample-docker-image")
        imageTag.set("0.0.1")
        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "ktor-app" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
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
