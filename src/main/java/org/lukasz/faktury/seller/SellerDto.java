package org.lukasz.faktury.seller;

public record SellerDto(String name, String nip, String regon, String city, String zipCode, String street,
                        String houseNumber) {
}
