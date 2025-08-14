// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.dynamic.feature) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}