plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.21" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}