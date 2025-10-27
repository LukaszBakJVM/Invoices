package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.dto.ConfirmPassword;

public interface ResetPasswordService {
    void createToken(String email);

    void findToken(String token,String email);
    void newPassword(ConfirmPassword confirmPassword,String email);
}
