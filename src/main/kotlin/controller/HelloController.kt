package es.unizar.webeng.hello.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.http.ResponseCookie
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.time.LocalTime
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.NoSuchMessageException
import org.springframework.web.bind.annotation.RequestHeader
import java.util.Locale

// @Controller indicates that this class is a servlet, so Spring can start it and begin serving pages. 
// It is a specialization of the @Component annotation, so Spring will detect it automatically. 
@Controller
class HelloController {
    @Value("\${app.message}")
    private var defaultMessage: String = "Hello World"

    // This annotation allows Spring to resolve and inject collaborating beans into our bean.
    // It can be used on fields, constructors, and methods
    @Autowired
    private lateinit var messageSource: MessageSource
    
    // Variables that store the greetings for each time of the day. They are read from the application.properties file.

    @Value("\${app.greeting.morning}")
    private var morningGreeting: String = "Good Morning"

    @Value("\${app.greeting.afternoon}")
    private var afternoonGreeting: String = "Good Afternoon"

    @Value("\${app.greeting.evening}")
    private var eveningGreeting: String = "Good Evening"

    @Value("\${app.greeting.night}")
    private var nightGreeting: String = "Good Night"

    
    // This method returns a greeting based on the current time of the day.
    private fun getTimeBasedGreeting(): String {
        val currentTime = LocalTime.now()
        return when (currentTime.hour) {
            in 6..11 -> morningGreeting // De 6:00 a 11:59
            in 12..17 -> afternoonGreeting // De 12:00 a 17:59
            in 18..21 -> eveningGreeting // De 18:00 a 21:59
            else -> nightGreeting // De 22:00 a 5:59
        }
    }

    /* This method receives http GET requests at the root of the server and returns the welcome template, 
    * which is in the resources/templates folder. The model is a map that contains the message attribute.
    * The message attribute is set to the value of the message variable, which is read from the 
    * application.properties file.
    * Cookie System: 
    * The method receives two optional parameters: backgroundColor and fontStyle. These parameters are 
    * annotated with @CookieValue, which indicates that the value of the parameter should be read from a cookie.
    * The defaultValue attribute of the annotation sets a default value for the parameter if the cookie is not present.
    * https://docs.spring.io/spring-framework/reference/web/webflux/controller/ann-methods/cookievalue.html 
    */
    @GetMapping("/") 
    fun welcome(model: MutableMap<String,Any>, 
                @CookieValue(value = "backgroundColor", defaultValue = "#FFFFFF") backgroundColor: String, 
                @CookieValue(value = "fontStyle", defaultValue = "Arial") fontStyle: String,
                @AuthenticationPrincipal userDetails: UserDetails?
                ): String {
                    var testMessage: String
                    try {
                        // Get the greeting message from the messages.properties file
                        testMessage = messageSource.getMessage("greeting", null, LocaleContextHolder.getLocale())
                        println(messageSource)
                    } catch (e: NoSuchMessageException) {
                        e.printStackTrace() // Esto imprimirá la excepción en la consola
                        println("Error al obtener el mensaje")
                        println(messageSource)
                        testMessage = "Error al obtener el mensaje"
                    }
                    val greetingMessage = getTimeBasedGreeting()
                    // If user is auntenthicated, add a personalized message
                    val personalizedMessage = if (userDetails != null) {
                        "$greetingMessage ${userDetails.username}!, $defaultMessage"
                    } else {
                        "$greetingMessage, $defaultMessage"
                    }
                    model["message"] = "$personalizedMessage"
                    model["backgroundColor"] = backgroundColor
                    model["fontStyle"] = fontStyle
                    return "welcome" 
    }

    // This method receives http POST requests at the /setTheme path. It receives two parameters: backgroundColor and fontStyle.
    // These parameters are annotated with @RequestParam, which indicates that the value of the parameter should be read from the request.
    // (https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/requestparam.html)
    // The method returns a redirection to the root of the server with the new theme set.
    @PostMapping("/setTheme")
    fun setTheme(
        @RequestParam backgroundColor: String,
        @RequestParam fontStyle: String
    ): ResponseEntity<Void> {
        // Create the cookies with the new values. The cookies are set to expire in 1 week.
        // They are valid in the whole application (/). 
        // (https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseCookie.html)
        val backgroundColorCookie = ResponseCookie.from("backgroundColor", backgroundColor)
            .maxAge(7 * 24 * 60 * 60) // 1 week
            .path("/")
            .build()
        val fontStyleCookie = ResponseCookie.from("fontStyle", fontStyle)
            .maxAge(7 * 24 * 60 * 60) // 1 week
            .path("/")
            .build()

        // Create the headers with the cookies and return a redirection to the root of the server.
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpHeaders.html
        val headers = HttpHeaders()
        headers.add(HttpHeaders.SET_COOKIE, backgroundColorCookie.toString())
        headers.add(HttpHeaders.SET_COOKIE, fontStyleCookie.toString())

        return ResponseEntity.status(302).header(HttpHeaders.LOCATION, "/").headers(headers).build()
    }

    // This method receives http GET requests at the /login path and returns the login template,
    @GetMapping("/login")
    fun login(): String {
        return "login"
    }
}
