plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 23

        consumerProguardFiles("consumer-rules.pro")
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
    namespace = "com.chenyue404.androidlib"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.lifecycle:lifecycle-common:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.fragment:fragment:1.5.7")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.code.gson:gson:2.9.1")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.chenyue404"
                artifactId = "androidlib"
                version = "1.0.0"

                pom.withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations.implementation.get().allDependencies.forEach { dp ->
                        if (dp.version != "unspecified") { // 过滤项目内library引用
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", dp.group)
                            dependencyNode.appendNode("artifactId", dp.name)
                            dependencyNode.appendNode("version", dp.version)
                            // for exclusions
//                            if (dp.excludeRules.size() > 0) {
//                                val exclusions = dependencyNode.appendNode("exclusions")
//                                dp.excludeRules.each { ex ->
//                                    val exclusion = exclusions.appendNode("exclusion")
//                                    exclusion.appendNode("groupId", ex.group)
//                                    exclusion.appendNode("artifactId", ex.module)
//                                }
//                            }
                        }
                    }
                }
            }
        }
    }
}