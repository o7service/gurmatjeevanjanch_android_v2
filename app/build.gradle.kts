plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id ("kotlin-parcelize")
}
android {
    namespace = "com.o7services.gurmatjeevanjaach"
    compileSdk = 36
    packaging {
        resources.excludes.add("META-INF/**")
    }
    defaultConfig {
        applicationId = "com.o7services.gurmatjeevanjaach"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures.viewBinding = true
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.translate)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.github.bumptech.glide:glide:4.10.0")
    kapt ("com.github.bumptech.glide:compiler:4.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.mlkit:translate:17.0.3")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:0.32")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
}