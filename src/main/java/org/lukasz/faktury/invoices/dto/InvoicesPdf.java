package org.lukasz.faktury.invoices.dto;

import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.views.invoice.TotalValues;

import java.util.List;

public record InvoicesPdf(InvoicesDto invoicesDto, BuyerDto buyerDto, List<InvoiceItemsDto> invoiceItemsDto,
                          TotalValues totalValues) {
}
