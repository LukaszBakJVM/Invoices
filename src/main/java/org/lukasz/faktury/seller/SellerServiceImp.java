package org.lukasz.faktury.seller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerServiceImp implements SellerService {
    private final SellerRepo repository;
    private final SellerMapper mapper;

    public SellerServiceImp(SellerRepo repository, SellerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override

    public Seller save(SellerDto dto) {

        Seller entity = mapper.toEntity(dto);
      return   repository.save(entity);

    }

    @Override
    public SellerDto findByUserEmail() {
        Seller seller = getAuthentication();
        return mapper.entityToDto(seller);

    }

    @Override
    public Seller findByEmail() {
        return  getAuthentication();


    }

    @Override
    @Transactional
    public void addAccountNb(String nb) {
        Seller seller = getAuthentication();
        seller.setAccountNb(nb);
        repository.save(seller);



    }

    private Seller getAuthentication (){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUserEmail(email).orElseThrow();
    }


}

