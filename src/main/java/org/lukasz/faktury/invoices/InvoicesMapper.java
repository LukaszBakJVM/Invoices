package org.lukasz.faktury.invoices;

import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InvoicesMapper {
    Invoices ToEntity(InvoicesDto dto, LocalDate generatedDateOfIssue ){
        Invoices invoices = new Invoices();
        invoices.setNumber(dto.number());
        invoices.setDateOfIssue(dto.dateOfIssue());
        invoices.setPlace(dto.place());
        invoices.setDateOfSale(dto.dateOfSale());
        invoices.setPostponement(dto.postponement());
        invoices.setPaymentDate(dto.paymentDate());
        invoices.setTypOfPayment(dto.TypOfPayment());
        invoices.setGeneratedDateOfIssue(generatedDateOfIssue);
        return invoices;


    }
}
