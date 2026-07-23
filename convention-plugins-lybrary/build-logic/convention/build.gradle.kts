import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

group = "com.9m72 3z88r z00z3039z802r    .buildlogic" // Package name

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("androidApplicationCompose") {
            id = "com.buildlogicconventionsettingsgradle.convention.application.compose" // This is the id we used to resolve our plugin.
            implementationClass = "com.example.convention.AndroidApplicationComposeConventionPlugin"
        }
        create("androidLibraryCompose"){
            id = "dfalksdjalsdj"
            implementationClass = "com.example.convention.AndroidLibraryComposeConventionPlugin"
        }
    }
}