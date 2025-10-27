package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.dto.ResetPasswordDto;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordMapper {
    ChangePassword toEntity(ResetPasswordDto resetPasswordDto) {
        ChangePassword password = new ChangePassword();
        password.setToken(resetPasswordDto.token());
        password.setDuration(resetPasswordDto.duration());
        password.setUser(resetPasswordDto.user());

        return password;
    }
}
