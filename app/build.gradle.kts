import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.0"
    id("com.github.jk1.dependency-license-report") version "2.9"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
    id("com.diffplug.spotless") version "8.0.0"
}

ktlint {
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(true)
    enableExperimentalRules.set(true)
}

licenseReport {
    outputDir = "${layout.buildDirectory}/reports/dependency-license"
    renderers =
        arrayOf(
            com.github.jk1.license.render
                .JsonReportRenderer(),
        )
}

android {
    namespace = "com.dpadninja.iromium"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dpadninja.iromium"
        minSdk = 30
        targetSdk = 36
        versionName = "0.0.3"
        versionCode = (System.currentTimeMillis() / 1000).toInt()
    }

    splits {
        abi {
            isEnable = true
            isUniversalApk = true
            exclude("armeabi", "arm64", "mips64", "mips", "riscv64", "x86", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    buildToolsVersion = "35.0.1"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    // Core Android
    implementation(libs.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Activity & Lifecycle
    implementation(libs.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.runtime.livedata)

    // Jetpack Compose UI
    implementation(libs.foundation)
    implementation(libs.ui.tooling)
    implementation(libs.ui.test.junit4)

    // Material Design 3
    implementation(libs.material3)
    implementation(libs.material.icons.extended)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.datastore.preferences)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}


tasks.register("renameReleaseApk") {
    doLast {
        val outputDir = File("app/build/outputs/apk/release")
        val newPrefix = "iromium"
        println("$outputDir")
        outputDir.listFiles()
            ?.filter { it.name.startsWith("app") && it.name.endsWith(".apk") }
            ?.forEach { file ->
                val newName = file.name.replaceFirst("app", newPrefix)
                val newFile = File(file.parentFile, newName)
                if (newFile.exists()) newFile.delete()
                file.renameTo(newFile)
                println("Renamed ${file.name} → ${newName}")
            }
    }
}

afterEvaluate {
    tasks.named("assembleRelease") {
        finalizedBy("renameReleaseApk")
    }
}

