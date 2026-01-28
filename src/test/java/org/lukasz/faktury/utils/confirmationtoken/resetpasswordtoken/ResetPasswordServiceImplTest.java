package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.user.User;
import org.lukasz.faktury.user.UserRepository;
import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordServiceImplTest {
    @Mock
    ChangePasswordRepo changePasswordRepo;
    @Mock
    ResetPasswordMapper resetPasswordMapper;
    @Mock
    EmailSenderService emailSenderService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    ResetPasswordServiceImpl resetPasswordService;

    @Test
    void shouldUpdateExistingChangePasswordAndSendEmail() {

        //given
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);

        ChangePassword changePassword = new ChangePassword();
        changePassword.setToken("generated-token");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(changePasswordRepo.findByUserEmail(email)).thenReturn(Optional.of(changePassword));
        doNothing().when(emailSenderService).sendEmail(anyString(), anyString());
        when(changePasswordRepo.save(any(ChangePassword.class))).thenReturn(changePassword);


        //when

        resetPasswordService.createToken(email);

        //then

        verify(changePasswordRepo).save(changePassword);
        verify(emailSenderService).sendEmail(eq(email), anyString());

        Assertions.assertNotNull(changePassword.getToken());
        Assertions.assertNotEquals("generated-token", changePassword.getToken());
        Assertions.assertNotNull(changePassword.getDuration());
        Assertions.assertFalse(changePassword.isUsed());

    }


    @Test
    void shouldCreateTokenForResetPasswordAndSendEmail() {
        //given
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);

        ChangePassword changePassword = new ChangePassword();
        changePassword.setToken("generated-token");
        changePassword.setDuration(LocalDateTime.now().plusHours(24));
        changePassword.setUser(user);


        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(changePasswordRepo.findByUserEmail(anyString())).thenReturn(Optional.empty());
        when(resetPasswordMapper.toEntity(any(ResetPasswordDto.class))).thenReturn(changePassword);

        //when
        resetPasswordService.createToken(email);

        //then

        verify(changePasswordRepo).save(changePassword);
        verify(emailSenderService).sendEmail(eq(email), anyString());
        verify(resetPasswordMapper).toEntity(any(ResetPasswordDto.class));


        Assertions.assertNotNull(changePassword.getToken());
        Assertions.assertEquals("generated-token", changePassword.getToken());
        Assertions.assertNotNull(changePassword.getDuration());
        Assertions.assertFalse(changePassword.isUsed());


    }

    @Test
    void shouldThrowExceptionWhenEmailForResetPasswordNotExist() {

        //given
        String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when then
        UserException response = assertThrows(UserException.class, () -> resetPasswordService.createToken(email));
        Assertions.assertEquals("Nie znaleziono uzytkownika test@test.com", response.getMessage());
    }

}
