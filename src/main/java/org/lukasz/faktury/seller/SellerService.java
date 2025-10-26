package org.lukasz.faktury.seller;

import org.lukasz.faktury.seller.dto.AccountNumber;
import org.lukasz.faktury.seller.dto.SellerDto;

public interface SellerService {
    Seller save(SellerDto dto);
    SellerDto findByUserEmail(String email);
    Seller findByEmail(String email);
    void addAccountNb(AccountNumber nb, String email);


}
