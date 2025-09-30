package org.lukasz.faktury.items;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceItemsService {
    List<String> tax();
    List<String>unit();
    int taxValue(String tax);
    BigDecimal nettoToBrutto (BigDecimal priceNetto , String tax);
    BigDecimal bruttoToNetto(BigDecimal priceBrutto ,String tax);
}
