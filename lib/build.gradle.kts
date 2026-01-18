import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.chenyue404.androidlib"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar() // 可选：如果你想同时发布源码
            // withJavadocJar() // 可选：如果你想同时发布 Javadoc
        }
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.lifecycle:lifecycle-common:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.fragment:fragment:1.8.9")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.code.gson:gson:2.13.2")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            // 此时 components["release"] 已经由上面的 singleVariant("release") 创建好了
            afterEvaluate {
                from(components["release"])
            }
            groupId = "com.github.chenyue404"
            artifactId = "androidlib"
            version = "1.0.0"
        }
    }
}