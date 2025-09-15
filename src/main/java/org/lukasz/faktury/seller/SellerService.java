package org.lukasz.faktury.seller;

public interface SellerService {
    Seller  save (SellerDto dto);
    SellerDto findByUserEmail();

}
