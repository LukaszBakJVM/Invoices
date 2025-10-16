package org.lukasz.faktury.buyer.dto;

public record BuyerDto(String name, String nip, String regon, String city, String zipCode, String street,
                       String houseNumber) {
}
