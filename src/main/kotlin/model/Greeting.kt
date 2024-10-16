package es.unizar.webeng.hello.model

import java.time.LocalDateTime

// Data class that represents a greeting
data class Greeting(
    val username: String,
    val message: String,
    val timestamp: LocalDateTime
)