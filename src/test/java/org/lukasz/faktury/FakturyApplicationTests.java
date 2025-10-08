package org.lukasz.faktury;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.lukasz.faktury.user.UserServiceImpl;
import org.lukasz.faktury.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")


class FakturyApplicationTests {
    @Autowired
    private UserServiceImpl userService;


    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withDatabaseName("invoices").withUsername("test").withPassword("test");


    static GreenMail greenMail;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();


    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("nipApi", wireMockServer::baseUrl);
        registry.add("tokenUrl", wireMockServer::baseUrl);
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
        registry.add("spring.mail.username", () -> "");
        registry.add("spring.mail.password", () -> "");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }


    @BeforeAll
    static void startPostgres() {
        postgreSQLContainer.start();
        greenMail = new GreenMail(ServerSetupTest.SMTP.dynamicPort());
        greenMail.start();


    }

    @AfterAll
    static void stopPostgres() {
        postgreSQLContainer.stop();
        greenMail.stop();

    }


    @Test
    @Transactional
    void shouldRegisterUserAndSendActivationEmailWithValidToken() {
        UserRequest request = new UserRequest("test@test.pl", "pass", "7151536825");

        userService.register(request);


    }
}
