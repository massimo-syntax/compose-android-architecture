plugins{
    `kotlin-dsl`
}

group = "com.example.conventionpluginandroidlibrarprojectlibsextensions.build-logic"

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin{
    plugins{
        register("android-library"){
            id = "my.convention.plugin.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}