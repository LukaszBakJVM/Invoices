package org.lukasz.faktury.invoices.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record InvoicesDto(@NotBlank String number, @NotBlank LocalDate dateOfIssue,@NotBlank String place, @NotBlank LocalDate dateOfSale, long postponement,
                          @NotBlank LocalDate paymentDate, @NotBlank String TyoOfPayment) {
}
