plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    // navigation-compose
    id("androidx.navigation.safeargs.kotlin")

    // ksp
    id("com.google.devtools.ksp")

    // compose compiler
    alias(libs.plugins.compose.compiler)

    kotlin("plugin.serialization") version "2.0.20"
}

android {
    namespace = "com.example.pokedex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokedex"
        minSdk = 21
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.foundation)

    // livedata
    implementation(libs.androidx.compose.runtime.livedata)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // navigation
    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json) // type-safety
    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)
    // ui
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    // layout
//    implementation(libs.androidx.constraintlayout)
//    //// to use constraintlayout in compose
//    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-beta01")

    //  images
    implementation(libs.glide.compose)

    // paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.runtime.ktx)

    ////  optional - Jetpack Compose integration
    implementation(libs.androidx.paging.compose)

    // api
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.gson)

    // persistence
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    //// To use Kotlin Symbol Processing (KSP)
    ksp(libs.androidx.room.compiler)

    //// optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    //// optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    //// optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)
}
