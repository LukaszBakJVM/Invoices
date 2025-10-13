package org.lukasz.faktury.seller;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellerMapper {
    List<Seller>sellers (List<SellerDto>sellerDtos){
        return sellerDtos.stream().map(this::toEntity).toList();
    }

    public Seller toEntity(SellerDto dto) {
        Seller seller = new Seller();
        seller.setName(dto.name());
        seller.setNip(dto.nip());
        seller.setRegon(dto.regon());
        seller.setCity(dto.city());
        seller.setZipCode(dto.zipCode());
        seller.setStreet(dto.street());
        seller.setHouseNumber(dto.houseNumber());
        return seller;

    }
    SellerDto entityToDto(Seller seller){
        return new SellerDto(seller.getName(), seller.getNip(), seller.getRegon(), seller.getCity(), seller.getZipCode(),seller.getStreet(), seller.getHouseNumber());
    }
}
