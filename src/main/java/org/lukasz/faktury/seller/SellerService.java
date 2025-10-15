package org.lukasz.faktury.seller;

import java.util.List;

public interface SellerService {
    Seller save(SellerDto dto);
    SellerDto findByUserEmail();
    Seller findByEmail();
    void addAccountNb(String nb);
    SellerDto selectCompany(List<SellerDto> companies,String company);

}
