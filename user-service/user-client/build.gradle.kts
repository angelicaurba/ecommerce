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
    implementation("org.springframework.security:spring-security-core:4.0.4.RELEASE")
    implementation("javax.validation:validation-api:2.0.0.Final")

    //adding internal modules dependency for dtos
    implementation(project(":common"))
}
