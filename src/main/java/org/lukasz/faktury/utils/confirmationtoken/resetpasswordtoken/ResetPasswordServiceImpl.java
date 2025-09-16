package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.exceptions.TokenException;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.user.User;
import org.lukasz.faktury.user.UserRepository;
import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    public ResetPasswordServiceImpl(ChangePasswordRepo changePasswordRepo, ResetPasswordMapper resetPasswordMapper, @Qualifier("resetPassword") EmailSenderService emailSenderService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.changePasswordRepo = changePasswordRepo;
        this.resetPasswordMapper = resetPasswordMapper;

        this.emailSenderService = emailSenderService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
            changePassword.setUsed(false);
            emailSenderService.sendEmail(user.getEmail(), link);
            changePasswordRepo.save(changePassword);
        } else {

            ResetPasswordDto resetPasswordDto = new ResetPasswordDto(uuid, duration, user);
            ChangePassword entity = resetPasswordMapper.toEntity(resetPasswordDto);
            entity.setUsed(false);
            changePasswordRepo.save(entity);

            emailSenderService.sendEmail(user.getEmail(), link);


        }


    }
//todo
    @Override
    public void findToken(String token) {
        changePasswordRepo.findByToken(token).orElseThrow(() -> new TokenException("Token nie istnieje"));


    }

    @Override
    public void newPassword(String token,String newPassword, String confirmPassword) {
        ChangePassword findToken = changePasswordRepo.findByToken(token).orElseThrow(() -> new TokenException("Token nie istnieje"));
        LocalDateTime expiresAt = findToken.getDuration();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenException("Token wygasł");
             } else if (!newPassword.equals(confirmPassword)) {
              throw new TokenException("Hasła nie są jednakowe");
        } else if (findToken.isUsed()) {
            throw new TokenException("Token juz zostal wykorzystany");
             } else {
                User user = userRepository.findByEmail(findToken.getUser().getEmail()).orElseThrow();
               user.setPassword(passwordEncoder.encode(newPassword));
               userRepository.save(user);
              findToken.setUsed(true);
              changePasswordRepo.save(findToken);

        }

    }

    private String link(final String token) {
        return UriComponentsBuilder.fromUriString(tokenUrl + "/resetToken?token={token}").buildAndExpand(token).toUriString();
    }
}
