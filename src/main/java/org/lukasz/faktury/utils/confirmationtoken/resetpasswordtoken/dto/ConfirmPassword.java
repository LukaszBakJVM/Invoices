package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.dto;

public record ConfirmPassword(String token,String newPassword,String confirmPassword) {
}
