import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "kolomyichuk.runly"
    compileSdk = 35

    defaultConfig {
        applicationId = "kolomyichuk.runly"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val localProperties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
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
        //Change before release
        debug {
            val debugApiKey = localProperties.getProperty("DEBUG_GOOGLE_MAP_API_KEY") ?: ""
            buildConfigField("String", "MAP_API_KEY", "\"$debugApiKey\"")
            manifestPlaceholders["GOOGLE_MAP_API_KEY"] = debugApiKey
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // Firebase
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.play.service.auth)

    // Credentials
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.identity.googleid)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Logging
    implementation(libs.timber)

    // Core AndroidX
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.data.store.preferences)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Google Maps
    implementation(libs.android.maps.utils)
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Jetpack Compose
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Permissions
    implementation(libs.accompanist.permissions)
}