package org.lukasz.faktury.items;

import org.lukasz.faktury.enums.Tax;
import org.lukasz.faktury.enums.Unit;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class InvoiceItemsServiceImpl implements InvoiceItemsService {
    private final InvoiceItemsRepo invoiceItemsRepo;
    private final InvoiceItemsMapper invoiceItemsMapper;

    public InvoiceItemsServiceImpl(InvoiceItemsRepo invoiceItemsRepo, InvoiceItemsMapper invoiceItemsMapper) {
        this.invoiceItemsRepo = invoiceItemsRepo;
        this.invoiceItemsMapper = invoiceItemsMapper;
    }

    @Override
    public List<String> tax() {
        return Arrays.stream(Tax.values()).map(Tax::name).toList();
    }


    @Override
    public List<String> unit() {
        return Arrays.stream(Unit.values()).map(Unit::getValue).toList();
    }
}
