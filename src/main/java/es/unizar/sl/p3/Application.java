package es.unizar.sl.p3;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Application {
    @Autowired
    private Input input;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*
     * @Bean
     * public CommandLineRunner run() {
     * return args -> {
     * input.startApp();
     * };
     * }
     */

}