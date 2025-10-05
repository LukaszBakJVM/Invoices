package org.lukasz.faktury.Buyer;

import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.springframework.stereotype.Component;

@Component
public class BuyerMapper {
  public   Buyer toEntity(BuyerDto dto){
        Buyer buyer = new Buyer();
        buyer.setName(dto.name());
        buyer.setNip(dto.nip());
        buyer.setRegon(dto.regon());
        buyer.setCity(dto.city());
        buyer.setZipCode(dto.zipCode());
        buyer.setStreet(dto.street());
        buyer.setHouseNumber(dto.houseNumber());
        return buyer;
    }
    BuyerDto entityToDto(Buyer buyer){
        return new BuyerDto(buyer.getName(), buyer.getNip(), buyer.getRegon(), buyer.getCity(), buyer.getZipCode(), buyer.getStreet(), buyer.getHouseNumber());
    }
}
