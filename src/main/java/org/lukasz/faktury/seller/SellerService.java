package org.lukasz.faktury.seller;

import java.util.List;

public interface SellerService {
   List< Seller> save (List<SellerDto> dto);
    SellerDto findByUserEmail();
    Seller findByEmail();
    void addAccountNb(String nb);

}
