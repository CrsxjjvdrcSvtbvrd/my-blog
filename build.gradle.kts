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
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    linuxX64 {
        binaries {
            executable()
        }
    }
    mingwX64 {
        binaries {
            executable()
        }
    }
    sourceSets {
        val jvmMain by getting
        val jvmTest by getting
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.okio:okio:3.3.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val linuxX64Main by getting
        val linuxX64Test by getting
        val mingwX64Main by getting {

        }
        val mingwX64Test by getting
    }
}

application {
    mainClass.set("MainKt")
}