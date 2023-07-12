plugins {
    kotlin("multiplatform") version "1.8.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val winTarget = when {
        hostOs == "Mac OS X" -> macosX64("win")
        hostOs == "Linux" -> linuxX64("win")
        isMingwX64 -> mingwX64("win")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    winTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    linuxX64("linux") {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val winMain by getting
        val winTest by getting
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.okio:okio:3.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val linuxMain by getting
        val linuxTest by getting
        val jvmMain by getting
        val jvmTest by getting
    }
}

application {
    mainClass.set("MainKt")
}