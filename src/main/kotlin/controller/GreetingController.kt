package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.service.GreetingService
import es.unizar.webeng.hello.model.Greeting
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GreetingController(private val greetingService: GreetingService) {

    @GetMapping("/greeting")
    fun getGreeting(@AuthenticationPrincipal userDetails: UserDetails?): Map<String, String> {
        val username = userDetails?.username ?: "Guest"
        val message = "Hello, welcome to our API!"
        greetingService.addGreeting(username, message)
        return mapOf("greeting" to message)
    }

    @GetMapping("/greeting/history")
    fun getGreetingHistory(): List<Greeting> {
        return greetingService.getGreetingHistory()
    }
}