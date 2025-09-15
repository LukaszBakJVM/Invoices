package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.lukasz.faktury.user.User;

public interface ActivationTokenService {
    void createToken(User user);
    void findToken(String token);

}
