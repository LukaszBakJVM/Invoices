package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.user.User;
import org.lukasz.faktury.user.UserRepository;
import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
}


