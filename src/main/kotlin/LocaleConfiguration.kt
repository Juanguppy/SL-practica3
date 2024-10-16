import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.Locale

@Configuration
class LocaleConfiguration : WebMvcConfigurer {

    // This method creates a SessionLocaleResolver object and sets the default locale to Locale.US.
    @Bean
    fun localeResolver(): LocaleResolver {
        val slr = SessionLocaleResolver()
        slr.setDefaultLocale(Locale.US)
        return slr
    }

    // This method creates a LocaleChangeInterceptor object and sets the parameter name to lang.
    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val lci = LocaleChangeInterceptor()
        lci.setParamName("lang")
        return lci
    }

    // This method adds the LocaleChangeInterceptor object to the InterceptorRegistry.
    // The LocaleChangeInterceptor object will intercept requests and change the locale based on the lang parameter.
    // The lang parameter is set in the request URL. For example, http://localhost:8080/?lang=es. 
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
    }

    // This method creates a ReloadableResourceBundleMessageSource object and sets the basename to messages.
    // The basename is the name of the base properties file. In this case, the base properties file is messages.properties.
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }
}