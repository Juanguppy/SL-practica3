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
        languageVersion.set(JavaLanguageVersion.of(17)) // Default to Java 17
    }
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.bootstrap)
    implementation("net.sourceforge.tess4j:tess4j:4.5.5")
    testImplementation(libs.spring.boot.starter.test)
}

tasks {
    bootJar {
        // Configura la clase principal
        mainClass.set("es.unizar.sl.p3.Application") // Asegúrate de que este sea el nombre completo
    }
}

// Task to switch to Java 21
tasks.register("useJava21") {
    doLast {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }
        tasks.withType<JavaCompile> {
            options.release.set(21)
        }
    }
}

// Task to switch back to Java 17
tasks.register("useJava17") {
    doLast {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
        tasks.withType<JavaCompile> {
            options.release.set(17)
        }
    }
}