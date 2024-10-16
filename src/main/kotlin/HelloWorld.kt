package es.unizar.webeng.hello

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.runApplication
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver

/* This anotation indicates that this class is a Spring Boot application and the main entry point of the application.
* It is equivalent to @Configuration @EnableAutoConfiguration @ComponentScan combined. 
* @Configuration: Tags the class as a source. It is used to define beans (building blocks).
* @EnableAutoConfiguration: Enables Spring Boot’s auto-configuration mechanism. This annotation 
*                           tells Spring Boot to “guess” how you want to configure Spring, based on the jar 
*                           dependencies that you have added.
* @ComponentScan: Tells Spring to look for other components, configurations, and services in the package. 
*                 They must be labeled as @Component or any of its children.    
* https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-using-springbootapplication-annotation.html
* https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-auto-configuration.html
* -------------------------------------------------------------------------------------------------------------- 
* // @Bean: Indicates that a method produces a bean to be managed by the Spring container. 
* Tutorial to internationalize a Spring Boot application: https://www.baeldung.com/spring-boot-internationalization 
*/ 
@SpringBootApplication
class Application 

fun main(args: Array<String>) {
    // This function is the entry point of the application. It delegates the call to the runApplication function.
    // The runApplication function creates an application context and starts the Spring application.
    runApplication<Application>(*args)
}
