package org.lukasz.faktury.items;

import org.lukasz.faktury.enums.Tax;
import org.lukasz.faktury.enums.Unit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class InvoiceItemsServiceImpl implements InvoiceItemsService {
    private final InvoiceItemsRepo invoiceItemsRepo;
    private final InvoiceItemsMapper invoiceItemsMapper;
    private final AtomicBoolean updating;

    public InvoiceItemsServiceImpl(InvoiceItemsRepo invoiceItemsRepo, InvoiceItemsMapper invoiceItemsMapper, AtomicBoolean updating) {
        this.invoiceItemsRepo = invoiceItemsRepo;
        this.invoiceItemsMapper = invoiceItemsMapper;
        this.updating = updating;
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
   public int taxValue(String tax){
      return   Tax.valueOf(tax).getVat();
    }
//todo zapetla sie brutto netto
    @Override
    public BigDecimal nettoToBrutto(BigDecimal priceNetto, String tax) {
        BigDecimal bigDecimal =  BigDecimal.ZERO;
        if (updating.get()) {

            if (getTax(tax) == 0) {
                return priceNetto;
            }

            BigDecimal calculateTax = BigDecimal.valueOf(getTax(tax)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            BigDecimal vat = priceNetto.multiply(calculateTax);
            BigDecimal brutto = priceNetto.add(vat);
            updating.set(false);
           bigDecimal = brutto.setScale(2, RoundingMode.HALF_UP);

        }
       return bigDecimal;
    }


    @Override
    public BigDecimal bruttoToNetto(BigDecimal priceBrutto, String tax) {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if (updating.get()) {
            if (getTax(tax) == 0) {
                return priceBrutto;
            }

        BigDecimal calculateTax = BigDecimal.ONE.add(BigDecimal.valueOf(getTax(tax)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        BigDecimal vat = priceBrutto.divide(calculateTax, 2, RoundingMode.HALF_UP);
        BigDecimal netto = priceBrutto.subtract(vat);
        updating.set(false);
          bigDecimal  = netto.setScale(2, RoundingMode.HALF_UP);



    }
        return bigDecimal;
        }

    private int getTax(String tax) {
        return Tax.valueOf(tax).getVat();
    }
}
