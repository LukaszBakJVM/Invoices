package org.lukasz.faktury.seller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

       Seller sellers = mapper.toEntity(dto);
        return   repository.save(sellers);

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

    @Override
    public SellerDto selectCompany(List<SellerDto> companies,String company) {
       return   companies.stream().filter(e->e.name().equals(company)).findFirst().get();

    }

    private Seller getAuthentication (){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUserEmail(email).orElseThrow();
    }


}

