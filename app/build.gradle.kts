plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.peterlopusan.traveldiary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.peterlopusan.traveldiary"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.appcompat:appcompat:1.7.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.3")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    implementation ("androidx.compose.material:material-icons-extended:1.7.3")

    //firebase auth
    implementation("com.google.firebase:firebase-auth:23.0.0")

    //firebase database
    implementation("com.google.firebase:firebase-database:21.0.0")

    //firebase storage
    implementation("com.google.firebase:firebase-storage:21.0.1")


    //RETROFIT API
    implementation ("com.squareup.okhttp3:logging-interceptor:3.12.1")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:okhttp-urlconnection:3.12.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Retrofit with Moshi Converter
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")

    //Glide
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")

    //Google maps
    implementation ("com.google.maps.android:maps-compose:4.4.1")
    implementation ("com.google.maps.android:android-maps-utils:3.8.2")

    //coil
    implementation ("io.coil-kt:coil-compose:2.1.0")

}