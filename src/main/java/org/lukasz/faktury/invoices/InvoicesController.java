package org.lukasz.faktury.invoices;

import org.lukasz.faktury.invoices.dto.CalculatePaymentDate;
import org.lukasz.faktury.invoices.dto.InvoicesDtoRequest;
import org.lukasz.faktury.invoices.dto.InvoicesNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoicesController {

    private final InvoicesService invoicesService;

    public InvoicesController(InvoicesService invoicesService) {
        this.invoicesService = invoicesService;
    }

    @PostMapping
    ResponseEntity<Void> createInvoices(@AuthenticationPrincipal UserDetails user, @RequestBody InvoicesDtoRequest request) {
        invoicesService.createInvoices(request, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/paymentsMethod")
    ResponseEntity<List<String>> paymentsMethod() {
        return ResponseEntity.ok(invoicesService.paymentsMethod());
    }

    @GetMapping("/invoicesNumber")
    ResponseEntity<InvoicesNumber> invoicesNumber(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(invoicesService.invoicesNumber(user.getUsername()));
    }

    @GetMapping("/calculatePaymentDate")
    ResponseEntity<CalculatePaymentDate> calculatePaymentDateResponse(@RequestParam LocalDate dateOfIssue, @RequestParam int postponement) {
        return ResponseEntity.ok(invoicesService.calculatePaymentDate(dateOfIssue, postponement));
    }
}
