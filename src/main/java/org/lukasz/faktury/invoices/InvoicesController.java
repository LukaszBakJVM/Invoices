package org.lukasz.faktury.invoices;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoicesController {
    private final InvoicesServiceImpl service;

    public InvoicesController(InvoicesServiceImpl service) {
        this.service = service;
    }


}
