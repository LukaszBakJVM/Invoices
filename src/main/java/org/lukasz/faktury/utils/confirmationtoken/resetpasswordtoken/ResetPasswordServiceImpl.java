package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.user.User;
import org.lukasz.faktury.user.UserRepository;
import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service

public class ResetPasswordServiceImpl implements ResetPasswordService {
    @Value("${tokenUrl}")
    private String tokenUrl;


    private final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);
    private final ChangePasswordRepo changePasswordRepo;
    private final ResetPasswordMapper resetPasswordMapper;
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;


    public ResetPasswordServiceImpl(ChangePasswordRepo changePasswordRepo, ResetPasswordMapper resetPasswordMapper, @Qualifier("resetPassword") EmailSenderService emailSenderService, UserRepository userRepository) {
        this.changePasswordRepo = changePasswordRepo;
        this.resetPasswordMapper = resetPasswordMapper;

        this.emailSenderService = emailSenderService;
        this.userRepository = userRepository;
    }
//todo
    @Override
    @Transactional
    public void createToken(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException(String.format("Nie znaleziono uzytkownika %s", email)));
        Optional<ChangePassword> byUserEmail = changePasswordRepo.findByUserEmail(email);
        String uuid = UUID.randomUUID().toString();
        LocalDateTime duration = LocalDateTime.now().plusHours(24);
        String link = link(uuid);
        if (byUserEmail.isPresent()) {
            ChangePassword changePassword = byUserEmail.get();
            changePassword.setToken(uuid);
            changePassword.setDuration(duration);
            emailSenderService.sendEmail(user.getEmail(), link);
            changePasswordRepo.save(changePassword);
        } else {

            ResetPasswordDto resetPasswordDto = new ResetPasswordDto(uuid, duration, user);
            ChangePassword entity = resetPasswordMapper.toEntity(resetPasswordDto);
            changePasswordRepo.save(entity);

            emailSenderService.sendEmail(user.getEmail(), link);


        }


    }
//todo
    @Override
    public void findToken(String token) {


    }
    private String link(final String token) {
        return UriComponentsBuilder.fromUriString(tokenUrl + "/change-password?change-password={token}").buildAndExpand(token).toUriString();
    }
}
