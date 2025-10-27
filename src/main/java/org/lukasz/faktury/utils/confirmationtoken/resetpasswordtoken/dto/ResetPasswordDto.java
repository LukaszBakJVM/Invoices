package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.dto;

import org.lukasz.faktury.user.User;

import java.time.LocalDateTime;

public record ResetPasswordDto(String token, LocalDateTime duration, User user) {
}
