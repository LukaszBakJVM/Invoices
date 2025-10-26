package org.lukasz.faktury.invoices.dto;

import java.math.BigDecimal;

public record TotalValues(BigDecimal totalNetto, BigDecimal totalVat, BigDecimal totalBrutto){}