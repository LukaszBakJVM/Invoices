package org.lukasz.faktury.invoices;

import org.lukasz.faktury.invoices.dto.InvoicesDto;

import java.util.List;

public interface InvoicesService {
    void createInvoices(InvoicesDto request);
    List<String> paymentsMethod();
    String invoicesNumber();


}
