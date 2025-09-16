package org.lukasz.faktury.invoices;

import org.springframework.stereotype.Service;

@Service
public class InvoicesServiceImpl implements InvoicesService{
    private final InvoicesRepo repo;
    private final InvoicesMapper mapper;


    public InvoicesServiceImpl(InvoicesRepo repo, InvoicesMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }
}
