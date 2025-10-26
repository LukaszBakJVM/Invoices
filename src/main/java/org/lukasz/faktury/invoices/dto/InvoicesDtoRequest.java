package org.lukasz.faktury.invoices.dto;

import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;

import java.util.List;

public record InvoicesDtoRequest(InvoicesDto invoicesDto, BuyerDto buyerDto, List<InvoiceItemsDto> invoiceItemsDtos) {
}
