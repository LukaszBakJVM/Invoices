package org.lukasz.faktury.invoices;

import org.lukasz.faktury.gusapi.NipApiResponse;
import org.lukasz.faktury.gusapi.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoicesController {
    private final InvoicesServiceImpl service;

    public InvoicesController(InvoicesServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{nip}")
    NipApiResponse ttt(@PathVariable String nip) {
        return service.result(nip);
    }
}
