package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Qualifier("resetPassword")
public class ResetPasswordServiceImpl implements ResetPasswordService,EmailSenderService{

    @Value("${nipApi}")
    private String baseUrl;
    private final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);
    private final ChangePasswordRepo changePasswordRepo;
    private final ResetPasswordMapper resetPasswordMapper;



    public ResetPasswordServiceImpl(ChangePasswordRepo changePasswordRepo, ResetPasswordMapper resetPasswordMapper) {
        this.changePasswordRepo = changePasswordRepo;
        this.resetPasswordMapper = resetPasswordMapper;

    }
//todo
    @Override
    public void createToken(String email) {
        ChangePassword findByEmail = changePasswordRepo.findByUserEmail(email).orElseThrow(() -> new UserException(String.format("Nie znaleziono uzytkownika %s", email)));


    }
//todo
    @Override
    public void findToken(String token) {


    }
//todo
    @Override
    public void sendEmail(String email, String link) {
        logger.info("Sending email to {} ", email);
        SimpleMailMessage send = new SimpleMailMessage();
        String message = String.format("Link aktywacyjny: %s", link);

        String subject = "Token aktywacyjny";
        send.setTo(email);
        send.setSubject(subject);
        send.setText(message);
        logger.info("Sending message {} ", message);

        //TODO
        // mailSender.send(send);


    }
    private String link(final String token) {
        return UriComponentsBuilder.fromUriString(token+ "/change-password?token={token}").buildAndExpand(token).toUriString();
    }
}
