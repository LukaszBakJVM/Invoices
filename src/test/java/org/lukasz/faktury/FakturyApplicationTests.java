package org.lukasz.faktury;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
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
import org.testcontainers.containers.PostgreSQLContainer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")


class FakturyApplicationTests {
    @Autowired
    private UserServiceImpl userService;
    static final int MAIL_PORT = 3025;


    @RegisterExtension
    static GreenMailExtension greenMailExtension = new GreenMailExtension(new ServerSetup(MAIL_PORT, null, ServerSetup.PROTOCOL_SMTP));
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withDatabaseName("invoices").withUsername("test").withPassword("test");



    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();



    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("nipApi", wireMockServer::baseUrl);
        registry.add("tokenUrl",wireMockServer::baseUrl);
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> MAIL_PORT);
        registry.add("spring.mail.username", () -> "");
        registry.add("spring.mail.password", () -> "");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }


    @BeforeAll
    static void startPostgres() {
        postgreSQLContainer.start();


    }

    @AfterAll
    static void stopPostgres() {
        postgreSQLContainer.stop();

    }

    @Test
    void aaaa() {
        UserRequest request = new UserRequest("test@test.pl", "pass", "7151536825");

        userService.register(request);


    }
}
