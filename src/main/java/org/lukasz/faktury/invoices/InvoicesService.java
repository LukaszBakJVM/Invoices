package org.lukasz.faktury.invoices;

import org.lukasz.faktury.invoices.dto.InvoicesDto;

import java.time.LocalDate;
import java.util.List;

public interface InvoicesService {
    void createInvoices(InvoicesDto request);
    List<String> paymentsMethod();
    String invoicesNumber();
    LocalDate calculatePaymentDate(LocalDate dateOfIssue,int postponement );


}
