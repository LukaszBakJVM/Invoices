package org.lukasz.faktury.items;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.sql.results.graph.collection.internal.BagInitializer;

import java.math.BigDecimal;

@Entity
public class InvoiceItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private int quantity;
    private String unit;
    private BigDecimal priceNetto;
    private int tax;
    private BigDecimal priceBrutto;
    private int discount;

    public InvoiceItems() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPriceNetto() {
        return priceNetto;
    }

    public void setPriceNetto(BigDecimal priceNetto) {
        this.priceNetto = priceNetto;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public BigDecimal getPriceBrutto() {
        return priceBrutto;
    }

    public void setPriceBrutto(BigDecimal priceBrutto) {
        this.priceBrutto = priceBrutto;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
