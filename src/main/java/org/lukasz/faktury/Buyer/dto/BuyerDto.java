package org.lukasz.faktury.Buyer.dto;

public record BuyerDto(String name, String nip, String regon, String city, String zipCode, String street,
                       String houseNumber) {
}
