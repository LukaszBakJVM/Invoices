package com.example.front.views.user.dto;

public record ConfirmPassword(String token,String newPassword,String confirmPassword,String email) {
}
