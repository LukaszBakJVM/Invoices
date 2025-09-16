package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

public interface ResetPasswordService {
    void createToken(String email);

    void findToken(String token);
    void newPassword(String token,String newPassword,String confirmPassword);
}
