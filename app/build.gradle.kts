import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}



android {
    namespace = "com.example.smartpark"
    compileSdk = 34
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    val keycloakBaseUrl: String = gradleLocalProperties(rootDir).getProperty("keycloakBaseUrl")
    val baseUrl: String = gradleLocalProperties(rootDir).getProperty("baseUrl")
    val monthlyPriceId: String = gradleLocalProperties(rootDir).getProperty("monthlyPriceId")
    val yearlyPriceId: String = gradleLocalProperties(rootDir).getProperty("yearlyPriceId")

    defaultConfig {
        applicationId = "com.example.smartpark"
        minSdk = 31
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
        getByName("debug") {
            buildConfigField("String", "keycloakBaseUrl", keycloakBaseUrl)
            buildConfigField("String", "baseUrl", baseUrl)
            buildConfigField("String", "monthlyPriceId", monthlyPriceId)
            buildConfigField("String", "yearlyPriceId", yearlyPriceId)
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("org.hildan.krossbow:krossbow-stomp-core:6.0.0")
    implementation("org.hildan.krossbow:krossbow-websocket-builtin:6.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.hildan.krossbow:krossbow-websocket-okhttp:6.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("io.ktor:ktor-client-android:1.6.4")
    implementation("io.ktor:ktor-client-websockets:1.6.4")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.google.maps.android:android-maps-utils:2.3.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    // Stripe
    implementation("com.stripe:stripe-android:20.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")


}