package org.lukasz.faktury.user;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.lukasz.faktury.exceptions.CustomValidationException;
import org.lukasz.faktury.exceptions.NipAlreadyRegisteredException;
import org.lukasz.faktury.exceptions.NipNotFoundException;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.dto.SellerDto;
import org.lukasz.faktury.seller.SellerRepo;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationEmailSenderServiceImpl;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationToken;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest()
@ActiveProfiles("test")
public class UserServiceIntegrationTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivationTokenRepo tokenRepository;
    @Autowired
    private SellerRepo sellerRepo;
    @MockBean
    private ActivationEmailSenderServiceImpl activationEmailSenderService;


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
    void shouldRegisterUser_AndSendActivationEmail_WithValidTokenCeidg() {
        UserRequest request = new UserRequest("7151536825@test.pl", "pass", "7151536825");

        SellerDto sellerDto = new SellerDto("Dolce Vita MARTA BĄK", "7151536825", "060293389", "Urzędów", "23-250", "ul. Rynek", "5");


      //  userService.register(request, sellerDto);

        User user = userRepository.findByEmail("7151536825@test.pl").orElseThrow();
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("7151536825@test.pl");
        assertThat(user.isActive()).isFalse();


        ActivationToken activationToken = tokenRepository.findByUser(user).orElseThrow();
        assertThat(activationToken.getToken()).isNotBlank();
        assertThat(activationToken.getExpiresAt()).isAfter(LocalDateTime.now());

        verify(activationEmailSenderService, times(1)).sendEmail(eq(user.getEmail()), anyString());


        Seller seller = sellerRepo.findByUserEmail("7151536825@test.pl").orElseThrow();

        assertThat(seller).isNotNull();
        assertThat(seller.getNip()).isEqualTo("7151536825");


    }


    @Test
    void shouldRegisterUser_AndSendActivationEmail_WithValidTokenMf() {
        UserRequest request = new UserRequest("5272962520@test.pl", "pass", "5272962520");

        SellerDto sellerDto = new SellerDto("BS z.o.o", "5272962520", "389300568", "WARSZAWA", "00-850", "PROSTA", "20");


       // userService.register(request, sellerDto);

        User user = userRepository.findByEmail("5272962520@test.pl").orElseThrow();
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("5272962520@test.pl");
        assertThat(user.isActive()).isFalse();


        ActivationToken activationToken = tokenRepository.findByUser(user).orElseThrow();
        assertThat(activationToken.getToken()).isNotBlank();
        assertThat(activationToken.getExpiresAt()).isAfter(LocalDateTime.now());

        verify(activationEmailSenderService, times(1)).sendEmail(eq(user.getEmail()), anyString());


        Seller seller = sellerRepo.findByUserEmail("5272962520@test.pl").orElseThrow();

        assertThat(seller).isNotNull();
        assertThat(seller.getNip()).isEqualTo("5272962520");


    }

    @Test
    void shouldRegisterUser_AndSendActivationEmail_WithValidToken_WhenNipRegisteredAndCompanyNotRegistered() {
        UserRequest request = new UserRequest("8133209246@test.pl", "pass", "8133209246");

        SellerDto sellerDto = new SellerDto("Software Soluzioni", "8133209246", "060557919", "WARSZAWA", "00-850", "PROSTA", "20");


      //  userService.register(request, sellerDto);

        User user = userRepository.findByEmail("8133209246@test.pl").orElseThrow();
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("8133209246@test.pl");
        assertThat(user.isActive()).isFalse();


        ActivationToken activationToken = tokenRepository.findByUser(user).orElseThrow();
        assertThat(activationToken.getToken()).isNotBlank();
        assertThat(activationToken.getExpiresAt()).isAfter(LocalDateTime.now());

        verify(activationEmailSenderService, times(1)).sendEmail(eq(user.getEmail()), anyString());


        Seller seller = sellerRepo.findByUserEmail("8133209246@test.pl").orElseThrow();

        assertThat(seller).isNotNull();
        assertThat(seller.getNip()).isEqualTo("8133209246");


    }


    @Test
    void shouldFindCompany_WhenSearchByNipCdeig(){
        List<SellerDto> dataByNip = userService.findDataByNip("8133209246");
        assertThat(dataByNip).hasSize(1);
    }

    @Test
    void shouldThrowException_WhenNipIsEmpty() {
        NipNotFoundException ex = assertThrows(NipNotFoundException.class, () -> userService.findDataByNip(""));
        assertEquals("Uzupełnij nip nabywcy", ex.getMessage());
    }

    @Test
    void shouldThrowException_WhenNipIsFault() {
        NipNotFoundException ex = assertThrows(NipNotFoundException.class, () -> userService.findDataByNip("1231234567"));
        assertEquals("Niepoprawny identyfikator NIP [1231234567]", ex.getMessage());

    }

    @Test
    void shouldFindCompany_WhenSearchByNipMf(){
        List<SellerDto> dataByNip = userService.findDataByNip("5272962520");
        assertThat(dataByNip).hasSize(1);
    }




    @Test
    void shouldNotRegisterUser_WhenCompanyIsRegistered_AndThrowException() {
        UserRequest request = new UserRequest("test4@test.pl", "pass", "8133209246");
        SellerDto sellerDto = new SellerDto("name1", "8133209246", "389300568", "WARSZAWA", "00-850", "PROSTA", "20");

      //  NipAlreadyRegisteredException ex = assertThrows(NipAlreadyRegisteredException.class, () -> userService.register(request, sellerDto));

      //  Assertions.assertEquals("Firma name1  8133209246 juz posiada konto ", ex.getMessage());
    }

    @Test
    void shouldNotRegisterUser_WhenNipIsIncorrect_AndThrowException() {
        UserRequest request = new UserRequest("5272962521@test.pl", "pass", "5272962521");
        SellerDto sellerDto = mock(SellerDto.class);

      //  CustomValidationException ex = assertThrows(CustomValidationException.class, () -> userService.register(request, sellerDto));

    //    Assertions.assertEquals("niepoprawny number identyfikacyjny VAT (NIP)", ex.getMessage());
    }

    @Test
    void shouldNotRegisterUser_WhenUserHaveAccount_AndThrowException() {
        UserRequest request = new UserRequest("test1@test.pl", "pass", "8133209246");
        SellerDto sellerDto = mock(SellerDto.class);

    //    UserException ex = assertThrows(UserException.class, () -> userService.register(request, sellerDto));

      //  Assertions.assertEquals("Użytkownik już posiada konto", ex.getMessage());
    }


}
