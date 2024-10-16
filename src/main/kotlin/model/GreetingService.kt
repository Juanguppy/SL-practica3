package es.unizar.webeng.hello.service

import es.unizar.webeng.hello.model.Greeting
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GreetingService {

    private val greetings = mutableListOf<Greeting>()

    // Add a greeting to the list
    fun addGreeting(username: String, message: String) {
        val greeting = Greeting(username, message, LocalDateTime.now())
        greetings.add(greeting)
    }

    // Get the list of greetings
    fun getGreetingHistory(): List<Greeting> {
        return greetings.toList()
    }
}