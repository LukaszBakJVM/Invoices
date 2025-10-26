package org.lukasz.faktury.invoices;

import org.lukasz.faktury.invoices.dto.CalculatePaymentDate;
import org.lukasz.faktury.invoices.dto.InvoicesDtoRequest;
import org.lukasz.faktury.invoices.dto.InvoicesNumber;

import java.time.LocalDate;
import java.util.List;

public interface InvoicesService {
    void createInvoices(InvoicesDtoRequest request,String email);
    List<String> paymentsMethod();

    InvoicesNumber invoicesNumber(String email);

    CalculatePaymentDate calculatePaymentDate(LocalDate dateOfIssue, int postponement);


}
