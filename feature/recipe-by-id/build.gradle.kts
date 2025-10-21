plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "com.rpm.recipe.by.id"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }

  buildFeatures {
    compose = true
  }

  buildTypes {
    release {
      isMinifyEnabled = true
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  testOptions {
    unitTests.isReturnDefaultValues = true
    unitTests.all {
      it.useJUnitPlatform()
    }
  }
}

kotlin {
  compilerOptions {
    jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
  }
}

dependencies {
  implementation(project(":domain:meal-domain"))
  implementation(project(":core:core-ui"))
  implementation(project(":core:core-common"))

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)

  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // Retrofit & OkHttp
  implementation(libs.retrofit)

  // Koin
  implementation(libs.koin.androidx.compose)

  // Coil for image loading
  implementation(libs.coil.compose)

  // Unit Test
  testImplementation(kotlin("test"))
  testImplementation(libs.junit.jupiter)
  testRuntimeOnly(libs.junit.vintage.engine)
  testImplementation(libs.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.mockwebserver)
}

apply(from = "$rootDir/config/ktlint/ktlint.gradle.kts")
