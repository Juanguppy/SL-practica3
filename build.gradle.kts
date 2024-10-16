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
    //https://spring.io/guides/gs/securing-web
    implementation("org.springframework.boot:spring-boot-starter-security")
    // Temporary explicit version to fix Thymeleaf bug
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.2.RELEASE")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks {
    bootJar {
        // Configura la clase principal
        mainClass.set("es.unizar.sl.p3.Application") // Asegúrate de que este sea el nombre completo
    }
}
