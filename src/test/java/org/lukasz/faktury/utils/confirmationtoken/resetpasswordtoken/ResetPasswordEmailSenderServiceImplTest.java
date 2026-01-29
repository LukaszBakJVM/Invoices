package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordEmailSenderServiceImplTest {
    @Mock
    JavaMailSender mailSender;
    @InjectMocks
    ResetPasswordEmailSenderServiceImpl resetPasswordEmailSenderService;

    @Test
    void shouldSendResetPasswordEmail() {
        // given
        String email = "test@test.pl";
        String link = "http://localhost/activate?token=123";

        // when
        resetPasswordEmailSenderService.sendEmail(email, link);

        // then
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        assertThat(sentMessage.getTo()).containsExactly(email);
        assertThat(sentMessage.getSubject()).isEqualTo("Token resetuajcy hasło");
        assertThat(sentMessage.getText()).isEqualTo("Link resetujacy hasło: " + link);
    }
}
