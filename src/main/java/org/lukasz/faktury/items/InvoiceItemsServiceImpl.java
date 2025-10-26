package org.lukasz.faktury.items;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.enums.Tax;
import org.lukasz.faktury.enums.Unit;
import org.lukasz.faktury.invoices.Invoices;
import org.lukasz.faktury.items.dto.*;
import org.lukasz.faktury.utils.validation.Validation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
public class InvoiceItemsServiceImpl implements InvoiceItemsService {
    private final InvoiceItemsRepo invoiceItemsRepo;
    private final InvoiceItemsMapper invoiceItemsMapper;
    private final Validation validation;


    public InvoiceItemsServiceImpl(InvoiceItemsRepo invoiceItemsRepo, InvoiceItemsMapper invoiceItemsMapper, Validation validation) {
        this.invoiceItemsRepo = invoiceItemsRepo;
        this.invoiceItemsMapper = invoiceItemsMapper;
        this.validation = validation;
    }

    @Override
    public List<String> tax() {
        return Arrays.stream(Tax.values()).map(Tax::name).toList();
    }


    @Override
    public List<String> unit() {
        return Arrays.stream(Unit.values()).map(Unit::getValue).toList();
    }



    @Override
    public NettoToBrutto nettoToBrutto(BigDecimal priceNetto, String tax) {


        if (getTax(tax) == 0) {
            return new NettoToBrutto(priceNetto);
            }


        BigDecimal calculateTax = BigDecimal.valueOf(getTax(tax)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            BigDecimal vat = priceNetto.multiply(calculateTax);
            BigDecimal brutto = priceNetto.add(vat);

        return new NettoToBrutto(brutto.setScale(2, RoundingMode.HALF_UP));


    }


    @Override
    public BruttoToNetto bruttoToNetto(BigDecimal priceBrutto, String tax) {


        if (getTax(tax) == 0) {
            return new BruttoToNetto(priceBrutto);
            }


        BigDecimal calculateTax = BigDecimal.ONE.add(BigDecimal.valueOf(getTax(tax)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return new BruttoToNetto(priceBrutto.divide(calculateTax, 2, RoundingMode.HALF_UP));



    }

    @Override
    public CalculateTotalValue calculateTotalValue(BigDecimal priceBrutto, int quantity) {
        return new CalculateTotalValue( priceBrutto.multiply(BigDecimal.valueOf(quantity)));
    }

    @Override
    public ReduceTotalValues reduceTotalValues(BigDecimal price, int quantity) {
        return new ReduceTotalValues( price.multiply(BigDecimal.valueOf(quantity)));

    }

    @Override
    @Transactional
    public void saveItems(List<InvoiceItemsDto> dtos, Invoices invoices) {
        List<InvoiceItems> invoiceItems = invoiceItemsMapper.saveItems(dtos,invoices);

        invoiceItemsRepo.saveAll(invoiceItems);


    }


    private int getTax(String tax) {
        return Tax.valueOf(tax).getVat();
    }
}
