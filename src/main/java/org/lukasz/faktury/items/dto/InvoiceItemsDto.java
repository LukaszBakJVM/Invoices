package org.lukasz.faktury.items.dto;

import java.math.BigDecimal;

public record InvoiceItemsDto(String description, int quantity, String unit, BigDecimal priceNetto, BigDecimal tax,
                              BigDecimal priceBrutto,BigDecimal totalValue) {
}
