// LawyerArchives/app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lawyer_archives"
    compileSdk = 34 // Target Android 14 (API 34)

    defaultConfig {
        applicationId = "com.lawyer_archives"
        minSdk = 24 // Minimum API level supported (Android 7.0 Nougat)
        targetSdk = 34 // Target API level for compilation and behavior changes
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Set to true for release builds to reduce APK size
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true // Enable View Binding for easier view interaction
    }

    kotlinOptions {
        jvmTarget = "1.8" // Specify JVM target compatibility for Kotlin
    }
}

dependencies {
    // AndroidX Core KTX for Kotlin extensions
    implementation("androidx.core:core-ktx:1.13.1")
    // AppCompat for backward compatibility of UI features
    implementation("androidx.appcompat:appcompat:1.6.1")
    // Material Design components for modern UI
    implementation("com.google.android.material:material:1.12.0")
    // ConstraintLayout for flexible UI layouts
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // RecyclerView for efficient list display
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // Activity KTX for activity-related Kotlin extensions (e.g., registerForActivityResult)
    implementation("androidx.activity:activity-ktx:1.9.0")
    // Fragment KTX for fragment-related Kotlin extensions
    implementation("androidx.fragment:fragment-ktx:1.7.1")
    // CardView for material design cards (used in item layouts)
    implementation("androidx.cardview:cardview:1.0.0") // Added for CardView in item layouts

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
