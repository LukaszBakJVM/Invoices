package org.lukasz.faktury.user;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationEmailSenderServiceImpl;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivationTokenRepo tokenRepository;
    @MockitoBean
    private ActivationEmailSenderServiceImpl activationEmailSenderService;


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

   // @Test
   //void shouldRegisterUser_AndSendActivationEmai_lWithValidToken() {
   //    UserRequest request = new UserRequest("test@test.pl", "pass", "7151536825");
   //    userService.register(request);

   //    User user = userRepository.findByEmail("test@test.pl").orElseThrow();
   //    assertThat(user).isNotNull();
   //    assertThat(user.getEmail()).isEqualTo("test@test.pl");
   //    assertThat(user.isActive()).isFalse();


   //    ActivationToken activationToken = tokenRepository.findByUser(user).orElseThrow();
   //    assertThat(activationToken.getToken()).isNotBlank();
   //    assertThat(activationToken.getExpiresAt()).isAfter(LocalDateTime.now());

   //    verify(activationEmailSenderService, times(1)).sendEmail(eq(user.getEmail()), anyString());


   //}

   //@Test
   //void shouldNotRegisterUser_WhenNipIsIncorrect_AndThrowException() {
   //    UserRequest request = new UserRequest("test1@test.pl", "pass", "5272962521");
   //    assertThrows(CustomValidationException.class, () -> userService.register(request));
   //}

   //@Test
   //void shouldNotRegisterUse_rWhenUserHaveAccount_AndThrowException() {
   //    UserRequest request = new UserRequest("test2@test.pl", "pass", "8133209246");

   //    assertThrows(UserException.class, () -> userService.register(request));
   //}


   //@Test
   //void shouldNotRegisterUser_WhenNipIsRegistered_AndThrowException() {
   //    UserRequest request = new UserRequest("test4@test.pl", "pass", "8733134841");

   //    assertThrows(NipAlreadyRegisteredException.class, () -> userService.register(request));
   //}


}
