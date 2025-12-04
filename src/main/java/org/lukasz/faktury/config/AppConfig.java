package org.lukasz.faktury.config;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.lukasz.faktury.views.user.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class AppConfig extends VaadinWebSecurity {
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
        return RestClient.builder().build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AtomicBoolean processFlag() {
        return new AtomicBoolean(false);
    }
}


