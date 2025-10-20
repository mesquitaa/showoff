plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "com.rpm.showoff"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.rpm.showoff"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
    buildConfig = true
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
  implementation(project(":core:core-network"))
  implementation(project(":core:core-ui"))
  implementation(project(":feature:category-list"))
  implementation(project(":feature:recipe-by-category-list"))

  // AndroidX
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // Navigation
  implementation(libs.androidx.navigation.compose)

  // ViewModel
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  // Retrofit & OkHttp
  implementation(libs.retrofit)
  implementation(libs.converter.gson)
  implementation(libs.logging.interceptor)

  // Coroutines
  implementation(libs.kotlinx.coroutines.android)

  // Dependency Injection
  implementation(libs.koin.android)
  implementation(libs.koin.androidx.compose)

  // Unit Test
  testImplementation(kotlin("test"))
  testImplementation(libs.junit.jupiter)
  testRuntimeOnly(libs.junit.vintage.engine)
  testImplementation(libs.coroutines.test)

  // Ui Test
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
}

apply(from = rootProject.file("config/ktlint/ktlint.gradle.kts"))
