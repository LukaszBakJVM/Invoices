package org.lukasz.faktury.buyer;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
@ActiveProfiles("test")

public class BuyerServiceImplIntegrationTest {


    @Autowired
    private BuyerServiceImpl buyerService;
    @Autowired
    private BuyerRepo repository;


    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withDatabaseName("invoices").withUsername("test").withPassword("test");

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();


    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("ceidgApi", wireMockServer::baseUrl);
        registry.add("mfApi", wireMockServer::baseUrl);
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());


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
    void shouldFindAndReturnBuyerFromDb_whenSearchByNip() {

        //given
        String nip = "1231234562";
        //when
        buyerService.findByNipAndSave(nip);


        //then
        wireMockServer.verify(0, getRequestedFor(urlPathEqualTo("/api/ceidg/v3/firma")));
        Buyer buyer = repository.findByNip(nip).getLast();
        assertThat(buyer.getName()).isEqualTo("test1buyer");
        assertThat(buyer.getRegon()).isEqualTo("0947464");


    }

    @Test
    void shouldFindAndReturnBuyerFromCdeig_whenSearchByNip() {
        //given
        String nip = "1231234569";
        //when
        buyerService.findByNipAndSave(nip);

        //then
        wireMockServer.verify(1, getRequestedFor(urlPathEqualTo("/api/ceidg/v3/firma")));
        Buyer buyer = repository.findByNip(nip).getLast();
        assertThat(buyer.getName()).isEqualTo("Test1 test");
        assertThat(buyer.getRegon()).isEqualTo("565665656");


    }
}
