plugins {
    alias(libs.plugins.spring.boot)
    id("java")
}

group = "es.unizar.sl"
version = "2024-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.bootstrap)
    testImplementation(libs.spring.boot.starter.test)
}

tasks {
    bootJar {
        // Configura la clase principal
        mainClass.set("es.unizar.sl.p3.Application") // Asegúrate de que este sea el nombre completo
    }
}
