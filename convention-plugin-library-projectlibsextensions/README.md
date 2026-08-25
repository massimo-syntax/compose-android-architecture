Installation

- build-logic folder with
    bild.gradle.kts
    settings.gradle.kts
                        files
- project root settings.gradle.kts
  pluginManagement {
      includeBuild("build-logic")
  ... }
(sync frequently)
- : build-logic/settings.gradle.kts needs dependencyResolutionManagement also to find libs catalog

- compileOnly()
- the plugins defied in gradlePlugins{} ,or class names, classes should be located at path: build-logic
    /src/main/kotlin , that has to be created
    or the class name does not resolve, at least not that easily
- in -> ./ext are the extensions files
- the project extension Project.configureKotlin ()
      configures ( KotlinAndroidExtension::class.java )
  this class is that defined in the build.gradle.kts build-logic
  compileOnly(libs.kotlin.gradlePlugin)
  see also libs.versions.toml

- with that done the actual convention plugins (classes) can access easily to the resources
- compileSdk = "37" can be in libs.versions.toml
  eventually also minSdk

- for simplicity to use in the modules, the id given in the file build-logic/build.gradle.kts
    @ gradlePlugin{
        plugins{
              register("android-library"){ 
                    id = "convention.plugin.android.library"
        ... }}}
  can be also defined in libs.versions.toml , something like
  [plugins]
  ...
  my-convention-library-plugin = { id = "my.convention.plugin.android.library" }
  then into the modules plugin {}
  can be imported like that