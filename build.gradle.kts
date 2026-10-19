// Root build file. Kotlin compilation is provided by AGP 9.x built-in Kotlin;
// the org.jetbrains.kotlin.android plugin must not be declared here.
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
