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
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.4")
    implementation("javax.validation:validation-api:2.0.0.Final")

    //adding internal modules dependency for dtos
    implementation(project(":common"))
    implementation(project(":order-service:order-client"))
}
