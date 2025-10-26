package org.lukasz.faktury.seller.dto;

public record SellerDto(String name, String nip, String regon, String city, String zipCode, String street,
                        String houseNumber) {
}
