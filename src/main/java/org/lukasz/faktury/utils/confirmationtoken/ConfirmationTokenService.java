package org.lukasz.faktury.utils.confirmationtoken;

import org.lukasz.faktury.user.User;

public interface ConfirmationTokenService {
    void createToken(User user);
    void findToken(String token);
}
