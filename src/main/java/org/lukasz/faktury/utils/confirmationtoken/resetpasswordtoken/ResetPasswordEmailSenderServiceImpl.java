package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationEmailSenderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Qualifier("resetPassword")
public class ResetPasswordEmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;

    Logger logger = LoggerFactory.getLogger(ActivationEmailSenderServiceImpl.class);

    public ResetPasswordEmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override

    public void sendEmail(String email, String link) {
        logger.info("Sending email to {} ", email);
        SimpleMailMessage send = new SimpleMailMessage();
        String message = String.format("Link resetujacy hasło: %s", link);

        String subject = "Token resetuajcy hasło";
        send.setTo(email);
        send.setSubject(subject);
        send.setText(message);
        logger.info("Sending message {} ", message);

        //TODO
         mailSender.send(send);


    }
}
