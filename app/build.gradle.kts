@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    val buildTime = System.currentTimeMillis()
    compileSdk = 33
    namespace = "com.xlp.verixsettings"
    defaultConfig {
        applicationId = namespace
        minSdk = 33
        targetSdk = 33
        versionCode = 114514
        versionName = "1.7R"
        buildConfigField("long", "BUILD_TIME", "$buildTime")
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro","proguard-log.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    androidResources {
        additionalParameters += arrayOf("--allow-reserved-package-id", "--package-id", "0x78")
    }
    packaging {
        resources {
            excludes += "**"
        }
        dex {
            useLegacyPackaging = true
        }
        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = "VerixSettings-$versionName-$name-$buildTime.apk"
            }
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

dependencies {
    compileOnly(libs.xposed.api)
    implementation(libs.core.ktx)
    implementation(libs.preference)
    implementation(project("::blockmiui"))
}

