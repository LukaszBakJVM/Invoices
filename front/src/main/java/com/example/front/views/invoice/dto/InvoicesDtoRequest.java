package com.example.front.views.invoice.dto;

import java.util.List;

public record InvoicesDtoRequest(InvoicesDto invoicesDto, BuyerDto buyerDto, List<InvoiceItemsDto> invoiceItemsDtos) {
}
