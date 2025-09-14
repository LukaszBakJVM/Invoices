package org.lukasz.faktury.utils.confirmationtoken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderServiceImpl implements EmailSenderService{
    private final JavaMailSender mailSender;

    Logger logger = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
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


        // mailSender.send(send);



    }
}
