package org.lukasz.faktury.utils.confirmationtoken;

public interface EmailSenderService {
    void sendEmail(String email,String link);
}
