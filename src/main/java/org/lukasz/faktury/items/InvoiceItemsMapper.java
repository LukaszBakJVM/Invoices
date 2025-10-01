package org.lukasz.faktury.items;

import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemsMapper {
    InvoiceItems dtoToEntity(InvoiceItemsDto dto) {
        InvoiceItems entity = new InvoiceItems();
        entity.setDescription(dto.description());
        entity.setQuantity(dto.quantity());
        entity.setUnit(dto.unit());
        entity.setPriceNetto(dto.priceNetto());
        entity.setTax(dto.tax());
        entity.setPriceBrutto(dto.priceBrutto());
        entity.setTotalValue(dto.totalValue());

        return entity;

    }
}
