package org.lukasz.faktury.Buyer;

import org.lukasz.faktury.Buyer.dto.BuyerDto;

public interface BuyerService {
    BuyerDto findByNipAndSave(String nip);
    Buyer findBuyer(String nip);
}
