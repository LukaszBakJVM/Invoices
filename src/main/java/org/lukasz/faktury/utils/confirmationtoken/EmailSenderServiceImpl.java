package org.lukasz.faktury.utils.confirmationtoken;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderServiceImpl implements EmailSenderService{
    private final JavaMailSender mailSender;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
   public void sendEmail(String email,String link){
        SimpleMailMessage message = new SimpleMailMessage();

        String subject = "Confirmation Token";
        message.setTo(email);
        message.setSubject(subject);
        message.setText(link);

        mailSender.send(message);



    }
}
