package org.lukasz.faktury.items;

import org.lukasz.faktury.invoices.Invoices;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceItemsService {
    List<String> tax();
    List<String>unit();

    BigDecimal nettoToBrutto (BigDecimal priceNetto , String tax);
    BigDecimal bruttoToNetto(BigDecimal priceBrutto ,String tax);
    BigDecimal calculateTotalValue(BigDecimal priceBrutto,int quantity);

    BigDecimal reduceTotalValues(BigDecimal price, int quantity);
    void saveItems(List<InvoiceItemsDto>dtos, Invoices invoices);

}
