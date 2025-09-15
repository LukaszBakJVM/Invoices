package org.lukasz.faktury.Buyer;

import org.lukasz.faktury.Buyer.dto.BuyerDto;

public interface BuyerService {
    BuyerDto findByNip(String nip);
}
