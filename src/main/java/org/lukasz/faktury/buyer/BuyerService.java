package org.lukasz.faktury.buyer;

import org.lukasz.faktury.buyer.dto.BuyerDto;

public interface BuyerService {
    BuyerDto findByNipAndSave(String nip);
    Buyer findBuyer(BuyerDto buyerDto);
    void findByNipAndName(BuyerDto buyerDto);

}
