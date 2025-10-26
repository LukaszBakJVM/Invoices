package org.lukasz.faktury.items;

import org.lukasz.faktury.invoices.Invoices;
import org.lukasz.faktury.items.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceItemsService {
    List<String> tax();
    List<String>unit();

    NettoToBrutto nettoToBrutto (BigDecimal priceNetto , String tax);
    BruttoToNetto bruttoToNetto(BigDecimal priceBrutto , String tax);
    CalculateTotalValue calculateTotalValue(BigDecimal priceBrutto, int quantity);

    ReduceTotalValues reduceTotalValues(BigDecimal price, int quantity);
    void saveItems(List<InvoiceItemsDto>dtos, Invoices invoices);

}
