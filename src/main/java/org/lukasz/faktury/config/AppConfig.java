package org.lukasz.faktury.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@EnableWebSecurity

public class AppConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/client/register").permitAll().anyRequest().authenticated()).httpBasic(Customizer.withDefaults());

        // http.csrf(AbstractHttpConfigurer::disable);
        return http.build();


    }
    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    StringBuilder stringBuilder() {
        return new StringBuilder();
    }
    @Bean
    public AtomicBoolean processFlag() {
        return new AtomicBoolean(false);
    }
}


