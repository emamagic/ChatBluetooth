import dependencies.*
plugins {
    id ("com.android.application")
    kotlin ("android")
    id ("androidx.navigation.safeargs.kotlin")
    kotlin ("kapt")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-parcelize")
}
android {
    compileSdkVersion  (Versions.compilesdk)
    buildToolsVersion = Versions.buildtools

    defaultConfig {
        applicationId = Application.id
        minSdkVersion (Versions.minsdk)
        targetSdkVersion (Versions.target)
        versionCode = Application.version_code
        versionName = Application.version_name
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true

        testInstrumentationRunner ("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility (JavaVersion.VERSION_1_8)
        targetCompatibility (JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = Java.java
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation (Dependencies.kotlin_standard_library)
    implementation (Dependencies.core)
    implementation (SupportDependencies.appcompat)
    implementation (SupportDependencies.material_design)
    implementation (SupportDependencies.constraintlayout)

    // Junit
    testImplementation (TestDependencies.junit4)
    androidTestImplementation (AndroidTestDependencies.ext_junit)

    // Espresso
    androidTestImplementation (AndroidTestDependencies.espresso)

    // Coroutines
    implementation(Dependencies.coroutines)
    androidTestImplementation(AndroidTestDependencies.coroutines)
    testImplementation(TestDependencies.coroutines)

    // Test Util
    androidTestImplementation(AndroidTestDependencies.truth)
    testImplementation(TestDependencies.truth)
    testImplementation(TestDependencies.arch_test)
    androidTestImplementation(AndroidTestDependencies.arch_test)
    androidTestImplementation(AndroidTestDependencies.rules_test)
    androidTestImplementation(AndroidTestDependencies.runner_test)



    // Navigation
    implementation(Dependencies.navigation_fragment)
    implementation(Dependencies.navigation_ui)

    // Retrofit
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_convertor)

    // Room
    implementation(Dependencies.room)
    implementation(Dependencies.room_ktx)
    kapt(AnnotationProcessing.room)

    //Hilt
    implementation(Dependencies.hilt)
    kapt(AnnotationProcessing.hilt)
    androidTestImplementation (AndroidTestDependencies.hilt)
    kaptAndroidTest(AnnotationProcessing.hilt_android_test)

    // Paging
    implementation(Dependencies.paging)

    // Multidex
    implementation(Dependencies.multidex)

    // Glide
    implementation(Dependencies.glide)
    kapt(AnnotationProcessing.glide)

    // Lifecycle
    implementation(Dependencies.livedata_lifecycle)
    implementation(Dependencies.viewmodel_lifecycle)

    // FragmentX
    implementation(Dependencies.fragmentx)
    debugImplementation(DebugDependencies.fragmentx)

}