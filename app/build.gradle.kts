plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")//APLICAR ESTA DEPENDENCIA
}

android {
    namespace = "com.example.appcynthiapena"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.appcynthiapena"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

         val room_version = "2.5.2"
         implementation("androidx.room:room-runtime:$room_version")
         kapt("androidx.room:room-compiler:$room_version") // Usamos kapt para la compilación de Room
         implementation("androidx.room:room-ktx:$room_version")
         // Corrutinas de AndroidX Lifecycle
         implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }