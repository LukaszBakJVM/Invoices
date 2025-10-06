package org.lukasz.faktury.views.invoice;

import java.math.BigDecimal;

public record TotalValues(BigDecimal totalNetto, BigDecimal totalVat, BigDecimal totalBrutto){}