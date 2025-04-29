plugins {
//    alias(libs.plugins.android.application)
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.quranapp"
    compileSdk = 35

    defaultConfig {
        multiDexEnabled
        applicationId = "com.example.quranapp"
        minSdk = 31
        targetSdk = 35
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
}

dependencies {
    implementation ("androidx.work:work-runtime-ktx:2.10.0")

    implementation ("androidx.multidex:multidex:2.0.1")

    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    implementation ("com.github.bumptech.glide:glide:4.15.1")

    // Room
    implementation ("androidx.room:room-runtime:2.7.0")
    implementation(libs.firebase.auth)
    annotationProcessor ("androidx.room:room-compiler:2.7.0")
    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.core:core:1.16.0")
    // Retrofit & Gson
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // ViewPager2
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.4.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}