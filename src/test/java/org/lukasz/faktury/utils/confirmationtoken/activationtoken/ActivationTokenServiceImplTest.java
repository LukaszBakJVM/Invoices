package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.exceptions.TokenException;
import org.lukasz.faktury.user.User;
import org.lukasz.faktury.user.UserRepository;
import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivationTokenServiceImplTest {
    @Mock
    ActivationTokenRepo tokenRepository;
    @Mock
    EmailSenderService emailSenderService;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ActivationTokenServiceImpl activationTokenService;

    @Test
    void shouldCreateTokenAndSendEmail() {
        // given
        User user = new User();
        user.setEmail("test@test.com");

        ActivationToken savedToken = new ActivationToken();
        savedToken.setToken("generated-token");

        when(tokenRepository.save(any(ActivationToken.class))).thenReturn(savedToken);

        // when
        activationTokenService.createToken(user);

        // then
        ArgumentCaptor<ActivationToken> tokenCaptor = ArgumentCaptor.forClass(ActivationToken.class);

        verify(tokenRepository).save(tokenCaptor.capture());
        verify(emailSenderService).sendEmail(eq("test@test.com"), contains("generated-token"));

        ActivationToken token = tokenCaptor.getValue();

        Assertions.assertNotNull(token.getToken());
        Assertions.assertNotNull(token.getExpiresAt());
        Assertions.assertEquals(user, token.getUser());
        Assertions.assertTrue(token.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Test
    void shouldActivateUserAndMarkTokenAsUsed_() {
        // given
        String tokenValue = "valid-token";

        User user = new User();
        user.setActive(false);

        ActivationToken token = new ActivationToken();
        token.setToken(tokenValue);
        token.setExpiresAt(LocalDateTime.now().plusHours(1));
        token.setUsed(false);
        token.setUser(user);

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<ActivationToken> tokenCaptor = ArgumentCaptor.forClass(ActivationToken.class);

        // when
        activationTokenService.findToken(tokenValue);

        // then
        verify(userRepository).save(userCaptor.capture());
        verify(tokenRepository).save(tokenCaptor.capture());

        User savedUser = userCaptor.getValue();
        ActivationToken savedToken = tokenCaptor.getValue();

        Assertions.assertTrue(savedUser.isActive());
        Assertions.assertTrue(savedToken.isUsed());
        Assertions.assertEquals(tokenValue, savedToken.getToken());
        Assertions.assertEquals(savedUser, savedToken.getUser());
    }



    @Test
    void shouldThrowException_WhenTokenExpired() {

        //given
        ActivationToken activationToken = new ActivationToken();
        activationToken.setToken("generated-token");
        activationToken.setExpiresAt(LocalDateTime.now().minusHours(25));


        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(activationToken));

        //when

        TokenException response = assertThrows(TokenException.class, () -> activationTokenService.findToken(anyString()));
        Assertions.assertEquals("Token wygasł", response.getMessage());

    }

    @Test
    void shouldThrowException_WhenTokenUsed() {

        //given
        ActivationToken activationToken = new ActivationToken();
        activationToken.setToken("generated-token");
        activationToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        activationToken.setUsed(true);


        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(activationToken));

        //then

        TokenException response = assertThrows(TokenException.class, () -> activationTokenService.findToken(anyString()));
        Assertions.assertEquals("Token już wykorzystany", response.getMessage());

    }
}


