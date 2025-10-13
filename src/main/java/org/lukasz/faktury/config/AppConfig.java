package org.lukasz.faktury.config;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.lukasz.faktury.views.user.LoginView;
import org.springframework.beans.factory.annotation.Value;
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


    @Value("${nipApi}")
    private String baseUrl;
    @Value("${tokenCeidg}")
    private String jwtToken;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        setLoginView(http, LoginView.class);
        http.formLogin(l->l.loginPage("/login")

                .defaultSuccessUrl("/dashbord", true));
    }
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + jwtToken)
                .build();
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


