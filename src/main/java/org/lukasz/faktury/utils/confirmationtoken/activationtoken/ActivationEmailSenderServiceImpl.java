package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Qualifier("accountActivation")
public class ActivationEmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;

    Logger logger = LoggerFactory.getLogger(ActivationEmailSenderServiceImpl.class);

    public ActivationEmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
   public void sendEmail(String email,String link){
        logger.info("Sending email to {} ", email);
        SimpleMailMessage send = new SimpleMailMessage();
        String message = String.format("Link aktywacyjny: %s", link);

        String subject = "Token aktywacyjny";
        send.setTo(email);
        send.setSubject(subject);
        send.setText(message);
        logger.info("Sending message {} ", message);

        //TODO
         mailSender.send(send);



    }
}
