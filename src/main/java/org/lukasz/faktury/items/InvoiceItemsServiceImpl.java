package org.lukasz.faktury.items;

import org.lukasz.faktury.enums.Tax;
import org.lukasz.faktury.enums.Unit;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.utils.validation.Validation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
    public BigDecimal nettoToBrutto(BigDecimal priceNetto, String tax) {


        if (getTax(tax) == 0) {
                return priceNetto;
            }


        BigDecimal calculateTax = BigDecimal.valueOf(getTax(tax)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            BigDecimal vat = priceNetto.multiply(calculateTax);
            BigDecimal brutto = priceNetto.add(vat);

        return brutto.setScale(2, RoundingMode.HALF_UP);


    }


    @Override
    public BigDecimal bruttoToNetto(BigDecimal priceBrutto, String tax) {


        if (getTax(tax) == 0) {
                return priceBrutto;
            }


        BigDecimal calculateTax = BigDecimal.ONE.add(BigDecimal.valueOf(getTax(tax)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return priceBrutto.divide(calculateTax, 2, RoundingMode.HALF_UP);



    }
   public List<InvoiceItemsDto>addPosition(InvoiceItemsDto position){
        List<InvoiceItemsDto>added = new ArrayList<>();
        validation.validation(position);
        added.add(position);
        return added;

    }

    private int getTax(String tax) {
        return Tax.valueOf(tax).getVat();
    }
}
