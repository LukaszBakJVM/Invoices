package com.example.front.config;


import com.example.front.views.user.LoginView;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class AppConfig extends VaadinWebSecurity {

    @Value("${apiinvoice}")
    private String apiInvoice;


    private final CustomAuthFailureHandler failureHandler;

    public AppConfig(CustomAuthFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        setLoginView(http, LoginView.class);
        http.formLogin(l -> l.loginPage("/login").failureHandler(failureHandler)

                .defaultSuccessUrl("/dashbord", true));
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl(apiInvoice).build();
    }


    @Bean
    public AtomicBoolean processFlag() {
        return new AtomicBoolean(false);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/client/register").permitAll().anyRequest().permitAll()).httpBasic(Customizer.withDefaults());

        // http.csrf(AbstractHttpConfigurer::disable);
        return http.build();

    }
}


