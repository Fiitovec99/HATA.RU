import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin") version "2.7.5"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.hataru"
    compileSdk = 35

    defaultConfig {
        val apikeyPropertiesFile = project.file("apikey.properties")
        val apikeyProperties = Properties()
        apikeyProperties.load(apikeyPropertiesFile.inputStream())

        buildConfigField("String", "USERNAME_KEY", apikeyProperties.getProperty("USERNAME_KEY"))
        buildConfigField("String", "PASSWORD_KEY", apikeyProperties.getProperty("PASSWORD_KEY"))
        buildConfigField("String", "MAP_API_KEY", apikeyProperties.getProperty("MAP_API_KEY"))


        applicationId = "com.example.hataru"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation ("com.airbnb.android:lottie:3.4.0")

    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")

    implementation ("io.insert-koin:koin-android:3.2.0")

    implementation("com.tbuonomo:dotsindicator:5.0")

    implementation("com.yandex.android:maps.mobile:4.4.0-full")

    implementation ("androidx.viewpager2:viewpager2:1.0.0")



    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation("com.google.android.gms:play-services-maps:18.2.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.core:core-ktx:+")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
}