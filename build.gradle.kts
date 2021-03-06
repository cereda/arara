// SPDX-License-Identifier: BSD-3-Clause

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.java.archives.internal.DefaultManifest
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.islandoftex.arara.build.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
    }
}

if (!project.hasProperty("jobToken")) {
    logger.warn("Will be unable to publish (jobToken missing)\n" +
            "Ignore this warning if you are not running the publish task " +
            "for the GitLab package repository.")
}

plugins {
    val versions = org.islandoftex.arara.build.Versions
    kotlin("jvm") version versions.kotlin apply false                                   // Apache 2.0
    kotlin("plugin.serialization") version versions.kotlin apply false                  // Apache 2.0
    id("com.github.johnrengelman.shadow") version versions.shadow apply false           // Apache 2.0
    id("com.github.ben-manes.versions") version versions.versionsPlugin                 // Apache 2.0
    id("com.diffplug.spotless-changelog") version versions.spotlessChangelog            // Apache 2.0
    id("org.jetbrains.dokka") version versions.dokka apply false                        // Apache 2.0
    id("io.gitlab.arturbosch.detekt") version versions.detekt                           // Apache 2.0
    id("com.diffplug.spotless") version versions.spotless                               // Apache 2.0
}

// exclude alpha and beta versions
// from the README (see https://github.com/ben-manes/gradle-versions-plugin)
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA")
                .any { candidate.version.toUpperCase().contains(it) }
        val isStable = stableKeyword || "^[0-9,.v-]+$".toRegex()
                .matches(candidate.version)
        isStable.not()
    }
}

spotlessChangelog {
    changelogFile("CHANGELOG.md")
    setAppendDashSnapshotUnless_dashPrelease(true)
    ifFoundBumpBreaking("breaking change")
    tagPrefix("v")
    commitMessage("Release v{{version}}")
    remote("origin")
    branch("master")
}

spotless {
    kotlinGradle {
        target("build.gradle.kts",
                "buildSrc/build.gradle.kts",
                "api/build.gradle.kts",
                "core/build.gradle.kts",
                "mvel/build.gradle.kts",
                "kotlin-dsl/build.gradle.kts",
                "cli/build.gradle.kts")
        targetExclude("src/test/**/*.kts")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlin {
        target("api/src/**/*.kt",
                "core/src/**/*.kt",
                "mvel/src/**/*.kt",
                "kotlin-dsl/src/**/*.kt",
                "cli/src/**/*.kt",
                "buildSrc/src/**/*.kt")
        targetExclude("src/test/**/*.kts")
        ktlint()
        licenseHeader("// SPDX-License-Identifier: BSD-3-Clause")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

detekt {
    failFast = false
    input = files(
            "api/src/main/kotlin",
            "core/src/main/kotlin",
            "mvel/src/main/kotlin",
            "kotlin-dsl/src/main/kotlin",
            "cli/src/main/kotlin",
            "buildSrc/src/main/kotlin"
    )
    buildUponDefaultConfig = true
    config = files("detekt-config.yml")
}

tasks.register("assembleCTANSourceZip", SourceZipBuilderTask::class.java)
tasks.register("assembleTDSTree", TDSTreeBuilderTask::class.java) {
    dependsOn(":cli:shadowJar")
    dependsOn(":docs:buildDocs")
    dependsOn("assembleCTANSourceZip")
}
tasks.register("assembleTDSZip", TDSZipBuilderTask::class.java) {
    dependsOn("assembleTDSTree")
}
tasks.register("assembleCTANTree", CTANTreeBuilderTask::class.java) {
    dependsOn("assembleTDSZip")
}
tasks.register("assembleCTAN", CTANZipBuilderTask::class.java) {
    dependsOn("assembleCTANTree")
}

version = spotlessChangelog.versionNext
allprojects {
    repositories {
        jcenter()
    }

    group = "org.islandoftex.arara"
    version = rootProject.version
}
subprojects {
    if (!path.contains("docs")) {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.dokka")

        dependencies {
            "implementation"(kotlin("stdlib", Versions.kotlin))
            "implementation"(kotlin("stdlib-jdk7", Versions.kotlin))
        }

        val javaCompatibility = JavaVersion.VERSION_1_8
        configure<JavaPluginExtension> {
            sourceCompatibility = javaCompatibility
            targetCompatibility = javaCompatibility

            withSourcesJar()
            withJavadocJar()
        }

        apply(plugin = "com.github.johnrengelman.shadow")
        val mainManifest: Manifest = DefaultManifest((project as ProjectInternal).fileResolver)
                .apply {
                    attributes["Implementation-Title"] = "arara-${project.name}"
                    attributes["Implementation-Version"] = version
                    if (project.name == "cli") {
                        attributes["Main-Class"] = "${project.group}.Arara"
                    }
                    // for Java 9+ compatibility of log4j
                    attributes["Multi-Release"] = "true"
                    attributes["Automatic-Module-Name"] = rootProject.group
                }
        tasks {
            withType<KotlinCompile> {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }

            withType<Jar> {
                archiveBaseName.set("arara-${project.name}")
                manifest.attributes.putAll(mainManifest.attributes)
            }
            named<Jar>("sourcesJar") {
                archiveBaseName.set("arara-${project.name}")
                archiveClassifier.set("sources")
            }
            named<Jar>("javadocJar") {
                archiveBaseName.set("arara-${project.name}")
                from(project.tasks.getByPath("dokkaJavadoc"))
                archiveClassifier.set("javadoc")
            }
            named<Jar>("shadowJar") {
                archiveBaseName.set("arara-${project.name}")
                archiveClassifier.set("with-deps")
            }

            withType<Test> {
                useJUnitPlatform()

                testLogging {
                    exceptionFormat = TestExceptionFormat.FULL
                    events(TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR,
                            TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.FAILED)
                }
            }
        }

        apply(plugin = "maven-publish")
        configure<PublishingExtension> {
            publications {
                register<MavenPublication>("GitLab") {
                    groupId = project.group.toString()
                    artifactId = "arara-${project.name}"
                    version = version

                    pom {
                        name.set("arara-${project.group}")
                        description.set("arara is a TeX automation tool based on " +
                                "rules and directives. It gives you a way to enhance " +
                                "your TeX experience.")
                        inceptionYear.set("2012")
                        url.set("https://gitlab.com/islandoftex/arara")
                        organization {
                            name.set("Island of TeX")
                            url.set("https://gitlab.com/islandoftex")
                        }
                        licenses {
                            license {
                                name.set("New BSD License")
                                url.set("http://www.opensource.org/licenses/bsd-license.php")
                                distribution.set("repo")
                            }
                        }
                        developers {
                            developer {
                                name.set("Paulo Roberto Massa Cereda")
                                email.set("cereda@users.sf.net")
                                id.set("cereda")
                                url.set("https://tex.stackexchange.com/users/3094")
                                roles.set(listOf("Lead developer", "Creator", "Duck enthusiast"))
                            }
                            developer {
                                name.set("Ben Frank")
                                id.set("benfrank")
                                url.set("https://gitlab.com/benfrank")
                                roles.set(listOf("Release coordinator v5 and v6"))
                            }
                            developer {
                                name.set("Marco Daniel")
                                email.set("marco.daniel@mada-nada.de")
                                id.set("marcodaniel")
                                url.set("https://tex.stackexchange.com/users/5239")
                                roles.set(listOf("Contributor", "Tester", "Fast driver"))
                            }
                            developer {
                                name.set("Brent Longborough")
                                email.set("brent@longborough.org")
                                id.set("brent")
                                url.set("https://tex.stackexchange.com/users/344")
                                roles.set(listOf("Developer", "Contributor", "Tester",
                                        "Haskell fanatic"))
                            }
                            developer {
                                name.set("Nicola Talbot")
                                email.set("nicola.lc.talbot@gmail.com")
                                id.set("nlct")
                                url.set("https://tex.stackexchange.com/users/19862")
                                roles.set(listOf("Developer", "Contributor", "Tester",
                                        "Hat enthusiast"))
                            }
                        }
                        scm {
                            connection.set("scm:git:https://gitlab.com/islandoftex/arara.git")
                            developerConnection.set("scm:git:https://gitlab.com/islandoftex/arara.git")
                            url.set("https://gitlab.com/islandoftex/arara")
                        }
                        ciManagement {
                            system.set("GitLab")
                            url.set("https://gitlab.com/islandoftex/arara/pipelines")
                        }
                        issueManagement {
                            system.set("GitLab")
                            url.set("https://gitlab.com/islandoftex/arara/issues")
                        }
                    }

                    artifact(tasks["sourcesJar"])
                    artifact(tasks["javadocJar"])
                    artifact(tasks["shadowJar"])
                    artifact(tasks["jar"])
                }

                System.getenv("CI_PROJECT_ID")?.let {
                    repositories {
                        maven {
                            url = uri("https://gitlab.com/api/v4/projects/$it/packages/maven")
                            credentials(HttpHeaderCredentials::class) {
                                if (project.hasProperty("jobToken")) {
                                    name = "Job-Token"
                                    value = project.property("jobToken").toString()
                                }
                            }
                            authentication {
                                create<HttpHeaderAuthentication>("header")
                            }
                        }
                    }
                }
            }
        }
    }
}
