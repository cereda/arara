// SPDX-License-Identifier: BSD-3-Clause

import org.islandoftex.arara.build.Versions
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    /*wasm32()
    js {
        browser {
            testTask {
                enabled = false
            }
        }
    }
    linuxArm64()
    linuxX64()
    macosX64()
    mingwX64()*/

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("org.islandoftex.arara.api.localization.AraraMessages")
            languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")

            dependencies {
                api(project(":api"))
                implementation("io.github.microutils:kotlin-logging:${Versions.kotlinLogging}")
                implementation("com.soywiz.korlibs.korio:korio:${Versions.korlibs}")
            }
        }
        val commonMain by getting {
            dependencies {
                kotlin("stdlib-common")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            languageSettings.useExperimentalAnnotation("kotlin.io.path.ExperimentalPathApi")
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}")
                implementation("com.charleskorn.kaml:kaml:${Versions.kaml}")
                implementation("org.zeroturnaround:zt-exec:${Versions.ztExec}")
                implementation("com.soywiz.korlibs.korio:korio:${Versions.korlibs}")
            }
        }
        val jvmTest by getting {
            languageSettings.useExperimentalAnnotation("kotlin.io.path.ExperimentalPathApi")
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("io.kotest:kotest-runner-junit5-jvm:${Versions.kotest}")
                implementation("io.kotest:kotest-assertions-core-jvm:${Versions.kotest}")
                runtimeOnly("org.slf4j:slf4j-simple:${Versions.slf4j}")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.junit}")
            }
        }
    }
}

tasks {
    named<Test>("jvmTest") {
        useJUnitPlatform()

        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR,
                    TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
        }
    }
}
