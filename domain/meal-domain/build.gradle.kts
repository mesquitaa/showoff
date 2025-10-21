plugins {
  id("java-library")
  alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
  compilerOptions {
    jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
  }
}

dependencies {
  // Coroutines
  implementation(libs.kotlinx.coroutines.android)

  implementation(libs.converter.gson)

  testImplementation(kotlin("test"))
//  testImplementation(libs.junit.jupiter)
//  testRuntimeOnly(libs.junit.vintage.engine)
  testImplementation(libs.converter.gson)
}

apply(from = rootProject.file("config/ktlint/ktlint.gradle.kts"))
