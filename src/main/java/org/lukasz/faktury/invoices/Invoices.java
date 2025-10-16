package org.lukasz.faktury.invoices;

import jakarta.persistence.*;
import org.lukasz.faktury.buyer.Buyer;
import org.lukasz.faktury.items.InvoiceItems;
import org.lukasz.faktury.seller.Seller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private LocalDate dateOfIssue;
    private String place;
    private LocalDate dateOfSale;
    private long postponement;
    private LocalDate paymentDate;
    private String typOfPayment;
    private LocalDateTime generatedDateOfIssue;
    @ManyToOne()
    private Seller seller;
    @ManyToOne()

    private Buyer buyer;


    public Invoices() {
    }

    @OneToMany(mappedBy = "invoices")
    List<InvoiceItems> items;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalDate getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(LocalDate dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public long getPostponement() {
        return postponement;
    }

    public void setPostponement(long postponement) {
        this.postponement = postponement;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public List<InvoiceItems> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItems> items) {
        this.items = items;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public String getTypOfPayment() {
        return typOfPayment;
    }

    public void setTypOfPayment(String typOfPayment) {
        this.typOfPayment = typOfPayment;
    }

    public LocalDateTime getGeneratedDateOfIssue() {
        return generatedDateOfIssue;
    }

    public void setGeneratedDateOfIssue(LocalDateTime generatedDateOfIssue) {
        this.generatedDateOfIssue = generatedDateOfIssue;
    }
}
