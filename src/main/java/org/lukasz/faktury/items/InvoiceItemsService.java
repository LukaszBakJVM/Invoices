package org.lukasz.faktury.items;

import org.lukasz.faktury.items.dto.InvoiceItemsDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceItemsService {
    List<String> tax();
    List<String>unit();

    BigDecimal nettoToBrutto (BigDecimal priceNetto , String tax);
    BigDecimal bruttoToNetto(BigDecimal priceBrutto ,String tax);
    BigDecimal calculateTotalValue(BigDecimal priceBrutto,int quantity);
    List<InvoiceItemsDto>addPosition(InvoiceItemsDto position);
}
