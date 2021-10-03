plugins {
    kotlin("multiplatform") version "1.5.10"
    `maven-publish`
    signing
}

group = "de.halfbit"
version = "1.0"

buildscript {
    repositories {
        mavenCentral()
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    js(LEGACY) {
        browser {

        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

val canPublishToMaven = project.hasProperty("signing.keyId")
if (canPublishToMaven) {

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    publishing {

        repositories {
            maven {
                name = "local"
                url = uri("$buildDir/repository")
            }
            maven {
                name = "central"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.getPropertyOrEmptyString("NEXUS_USERNAME")
                    password = project.getPropertyOrEmptyString("NEXUS_PASSWORD")
                }
            }
            maven {
                name = "snapshot"
                url = uri("https://oss.sonatype.org/content/repositories/snapshots")
                credentials {
                    username = project.getPropertyOrEmptyString("NEXUS_USERNAME")
                    password = project.getPropertyOrEmptyString("NEXUS_PASSWORD")
                }
            }
        }

        publications.withType<MavenPublication> {

            artifact(javadocJar.get())

            pom {
                name.set("okres")
                description.set("Concise result type representing success and error values")
                url.set("http://www.halfbit.de")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("halfbit")
                        name.set("Sergej Shafarenka")
                        email.set("info@halfbit.de")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:sergejsha/okres.git")
                    developerConnection.set("scm:git:ssh://github.com:sergejsha/okres.git")
                    url.set("http://www.halfbit.de")
                }
            }
        }
    }

    signing {
        sign(publishing.publications)
    }
}

fun Project.getPropertyOrEmptyString(name: String): String =
    if (hasProperty(name)) property(name) as String? ?: "" else ""
