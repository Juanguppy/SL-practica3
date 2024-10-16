package es.unizar.webeng.hello.controller

import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import java.time.LocalTime
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath



@WebMvcTest(HelloController::class)
class HelloControllerMVCTests {
    @Value("\${app.message}")
    private lateinit var defaultMessage: String

    @Value("\${app.greeting.morning}")
    private lateinit var morningGreeting: String

    @Value("\${app.greeting.afternoon}")
    private lateinit var afternoonGreeting: String

    @Value("\${app.greeting.evening}")
    private lateinit var eveningGreeting: String

    @Value("\${app.greeting.night}")
    private lateinit var nightGreeting: String

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userDetailsService: UserDetailsService

    private fun getExpectedGreeting(): String {
        val currentTime = LocalTime.now()
        return when (currentTime.hour) {
            in 6..11 -> morningGreeting
            in 12..17 -> afternoonGreeting
            in 18..21 -> eveningGreeting
            else -> nightGreeting
        }
    }

    @Test
    @WithMockUser(username = "user", roles = ["USER"])
    fun testMessage() {
        val expectedGreeting = getExpectedGreeting()
        val expectedMessage = "$expectedGreeting user!, $defaultMessage"

        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(model().attribute("message", equalTo(expectedMessage)))
    }

    @Test
    fun testLogin() {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk)
    }

}