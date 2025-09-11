package org.lukasz.faktury.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.pl.NIP;

public record UserRequest(@NotBlank(message = "nazwa użytkownika nie może byc pusta") String username,
                          @NotBlank(message = "hasło nie może byc puste") String password,
                          @NotBlank(message = "email  nie może byc pusty") @Email String email,
                          @NotBlank(message = "nip  nie może byc pusty") @NIP String nip) {
}
