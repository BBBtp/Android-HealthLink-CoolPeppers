plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}
hilt {
    enableAggregatingTask = false
}
android {
    namespace = "com.CoolPeppers.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.CoolPeppers.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.appcompat)
    implementation(libs.material.v1110)
    implementation(libs.androidx.constraintlayout)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v270)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Hilt
    implementation(libs.hilt.android)
    //implementation("androidx.storage:storage:1.1.0")
    implementation(libs.androidx.runtime.livedata)
    kapt(libs.hilt.compiler)
    implementation (libs.androidx.hilt.navigation.compose.v110)


    implementation("com.github.chaosleung:pinview:1.3.1")

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation (libs.androidx.room.paging)

    // ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // Glide
    implementation(libs.glide)

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    // Testing
    testImplementation (libs.junit)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.turbine)

    // Android Testing
    androidTestImplementation (libs.androidx.junit.v115)
    androidTestImplementation (libs.androidx.espresso.core.v351)
    androidTestImplementation (libs.ui.test.junit4)
    androidTestImplementation (libs.hilt.android.testing)
    kaptAndroidTest (libs.hilt.android.compiler)
    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material3)

    // Compose Navigation
    implementation (libs.androidx.navigation.compose.v277)

    // Lifecycle
    implementation (libs.androidx.lifecycle.runtime.compose)
    implementation (libs.androidx.lifecycle.viewmodel.compose.v270)

    // Coil для загрузки изображений
    implementation (libs.coil.compose)

    // SwipeRefresh
    implementation (libs.androidx.material)

    // Debug
    debugImplementation (libs.ui.tooling)
    debugImplementation (libs.ui.test.manifest)

    // ExoPlayer
    val media3_version = "1.2.1"
    implementation (libs.androidx.media3.exoplayer.v121)
    implementation (libs.androidx.media3.ui.v121)
    implementation (libs.androidx.media3.common)

    // Paging
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
}