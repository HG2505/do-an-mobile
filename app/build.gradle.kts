plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.giuaki"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.giuaki"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // --- XÓA DÒNG NÀY ĐI ĐỂ HẾT LỖI ---
    // implementation(libs.firebase.firestore.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- PHẦN FIREBASE CHUẨN ---
    // 1. Firebase BOM (Quản lý phiên bản chung)
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    // 2. Firebase Auth (Đăng nhập)
    implementation("com.google.firebase:firebase-auth")

    // 3. Firebase Firestore (Lưu trữ dữ liệu chữ)
    implementation("com.google.firebase:firebase-firestore")

    // 4. Firebase Storage (QUAN TRỌNG: Lưu trữ ảnh linh kiện)
    implementation("com.google.firebase:firebase-storage")

    // 5. Glide (Để hiển thị ảnh từ link Firebase lên App)
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Google Play Services Auth (cần thiết cho Google Sign In)
    implementation("com.google.android.gms:play-services-auth:20.7.0")
}