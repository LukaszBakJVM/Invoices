package org.lukasz.faktury.user.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(@NotBlank(message = "username cannot be blank") String username,
                                  @NotBlank(message = "password cannot be blank") String password,
                                  @NotBlank(message = "email cannot be blank") String email,
                                  @NotBlank(message = "nip cannot be blank") String nip) {
}
