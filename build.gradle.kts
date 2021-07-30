plugins {
    kotlin("jvm") version "1.5.21"
}

group = "it.polito.wa2"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
