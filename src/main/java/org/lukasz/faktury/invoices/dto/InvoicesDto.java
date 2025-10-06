package org.lukasz.faktury.invoices.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InvoicesDto(@NotBlank String number, @NotNull LocalDate dateOfIssue, @NotBlank String place, @NotNull LocalDate dateOfSale, long postponement,
                          @NotNull LocalDate paymentDate, @NotBlank String typOfPayment) {
}
