package ru.tech.telegrambot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Clock;

/**
 * Default application configuration.
 */
@Configuration
@ComponentScan("ru.tech")
@EnableWebMvc
@PropertySource("classpath:config/application.yml")
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
