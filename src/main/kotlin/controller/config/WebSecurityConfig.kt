package es.unizar.webeng.hello.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests {
                it.requestMatchers("/api/greeting/history").hasRole("ADMIN") // Only users with role ADMIN can access /admin
                it.requestMatchers("/**", "/webjars/**", "/css/**", "/login", "/api/greeting", "/setTheme").permitAll() // Permit access to these routes
                it.anyRequest().authenticated() // Other routes require authentication
            } 
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .accessDeniedPage("/login") // Redirect to /accessDenied if access is denied
            }
            .formLogin { form ->
                form
                    .loginPage("/login").permitAll() 
                    .defaultSuccessUrl("/", true)
            }
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login").permitAll()
            }
            .csrf() // Enable CSRF protection
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        // Create users (examples)
        val user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build()

        val user1 = User.builder()
            .username("user1")
            .password(passwordEncoder().encode("password1"))
            .roles("USER")
            .build()

        val user2 = User.builder()
            .username("user2")
            .password(passwordEncoder().encode("password2"))
            .roles("USER")
            .build()

        val user3 = User.builder()
            .username("user3")
            .password(passwordEncoder().encode("password3"))
            .roles("USER")
            .build()

        val admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build()

        return InMemoryUserDetailsManager(user, user1, user2, user3, admin)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
