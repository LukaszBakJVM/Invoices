package org.lukasz.faktury.Seller;

public record SellerDto(String name, String nip, String regon, String city, String zipCode, String street,
                        String houseNumber) {
}
