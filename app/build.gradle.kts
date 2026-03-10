plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lihan.smartstep"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.lihan.smartstep"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "false"
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
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
    resolutionStrategy.eachDependency {
        if (requested.group == "com.google.guava" && requested.name == "listenablefuture") {
            useVersion("9999.0-empty-to-avoid-conflict-with-guava")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)



    implementation(libs.splashscreen)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.navigation.compose)
    implementation(libs.google.accompanist.permissions)

    implementation(libs.bundles.koin)
    coreLibraryDesugaring(libs.desugar.jdk)

    implementation(libs.room.runtime)
    implementation(libs.room.compiler)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.work)
    androidTestImplementation(libs.work)
    androidTestImplementation(libs.work.testing)
    implementation(libs.serialization)

    implementation("com.google.guava:guava:31.1-android")
    androidTestImplementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

}