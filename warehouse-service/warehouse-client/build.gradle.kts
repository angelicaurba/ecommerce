//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "it.polito.wa2"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    //adding internal modules dependency for dtos
    implementation(project(":common"))
}
