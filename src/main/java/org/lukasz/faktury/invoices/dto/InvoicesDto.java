package org.lukasz.faktury.invoices.dto;

import jakarta.validation.constraints.NotBlank;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;

import java.time.LocalDate;
import java.util.List;

public record InvoicesDto(@NotBlank String number, @NotBlank LocalDate dateOfIssue, @NotBlank String place, @NotBlank LocalDate dateOfSale, long postponement,
                          @NotBlank LocalDate paymentDate, @NotBlank String TyoOfPayment, List<InvoiceItemsDto>invoiceItemsDto) {
}
