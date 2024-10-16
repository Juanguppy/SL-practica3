package es.unizar.webeng.hello.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalTime
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.mockito.Mockito
import org.springframework.context.MessageSource
import java.util.Locale


class HelloControllerUnitTests {
    private lateinit var controller: HelloController

    private val morningGreeting = "Good Morning"
    private val afternoonGreeting = "Good Afternoon"
    private val eveningGreeting = "Good Evening"
    private val nightGreeting = "Good Night"
    private val defaultMessage = "Hello World"
    private val defaultUser ="user"
    private lateinit var messageSource: MessageSource

    @BeforeEach
    fun setup() {
        messageSource = Mockito.mock(MessageSource::class.java)
        controller = HelloController()

        // Use reflection to set the messageSource field (not a good practice but as an example will work)
        // In a real project -> use constructor injection (modify the controller to receive the messageSource as a parameter)
        val field = HelloController::class.java.getDeclaredField("messageSource")
        field.isAccessible = true
        field.set(controller, messageSource)

        Mockito.`when`(messageSource.getMessage("greeting", null, Locale.US)).thenReturn("Hello")
    }
    @Test
    fun testMessage() {
        val map = mutableMapOf<String,Any>()
        
        val currentTime = LocalTime.now()
        val expectedGreeting = when (currentTime.hour) {
            in 6..11 -> morningGreeting
            in 12..17 -> afternoonGreeting
            in 18..21 -> eveningGreeting
            else -> nightGreeting
        }
        val expectedMessage = "$expectedGreeting $defaultUser!, $defaultMessage"
        
        // "cookies"
        val backgroundColor = "#FFFFFF"
        val fontStyle = "Arial"
        // user login
        val userDetails: UserDetails = User.withUsername("user")
        .password("password")
        .roles("USER")
        .build()

        val view = controller.welcome(map, backgroundColor, fontStyle, userDetails)
        assertThat(view).isEqualTo("welcome")
        assertThat(map.containsKey("message")).isTrue
        assertThat(map["message"]).isEqualTo(expectedMessage)
        assertThat(map["backgroundColor"]).isEqualTo(backgroundColor)
        assertThat(map["fontStyle"]).isEqualTo(fontStyle)
    }
}
