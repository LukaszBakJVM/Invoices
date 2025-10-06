package org.lukasz.faktury.invoices;

import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;

import java.time.LocalDate;
import java.util.List;

public interface InvoicesService {
    void createInvoices(InvoicesDto request, BuyerDto buyerDto, List<InvoiceItemsDto> invoiceItemsDtos);
    List<String> paymentsMethod();
    String invoicesNumber();
    LocalDate calculatePaymentDate(LocalDate dateOfIssue,int postponement );


}
