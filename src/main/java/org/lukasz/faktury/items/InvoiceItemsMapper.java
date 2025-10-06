package org.lukasz.faktury.items;

import org.lukasz.faktury.invoices.Invoices;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceItemsMapper {

    List<InvoiceItems>saveItems(List<InvoiceItemsDto>dtos,Invoices invoices){
        return dtos.stream().map(dto->dtoToEntity(dto,invoices)).toList();
    }

  private   InvoiceItems dtoToEntity(InvoiceItemsDto dto, Invoices invoices) {
        InvoiceItems entity = new InvoiceItems();
        entity.setDescription(dto.description());
        entity.setQuantity(dto.quantity());
        entity.setUnit(dto.unit());
        entity.setPriceNetto(dto.priceNetto());
        entity.setTax(dto.tax());
        entity.setPriceBrutto(dto.priceBrutto());
        entity.setTotalValue(dto.totalValue());
        entity.setInvoices(invoices);

        return entity;

    }
}
