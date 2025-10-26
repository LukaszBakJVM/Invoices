package org.lukasz.faktury.seller;

import org.lukasz.faktury.exceptions.NipAlreadyRegisteredException;
import org.lukasz.faktury.seller.dto.AccountNumber;
import org.lukasz.faktury.seller.dto.SellerDto;
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
        repository.findByNipAndName(dto.nip(), dto.name()).ifPresent(present->{throw new
                NipAlreadyRegisteredException(String.format("Firma %s  %s juz posiada konto ",dto.name(),dto.nip()));});

       Seller sellers = mapper.toEntity(dto);
        return   repository.save(sellers);

    }

    @Override
    public SellerDto findByUserEmail(String email) {
        Seller seller = getAuthentication(email);
        return mapper.entityToDto(seller);

    }

    @Override
    public Seller findByEmail(String email) {
        return getAuthentication(email);


    }

    @Override
    @Transactional
    public void addAccountNb(AccountNumber nb, String email) {
        Seller seller = getAuthentication(email);
        seller.setAccountNb(nb.number());
        repository.save(seller);



    }


    private Seller getAuthentication(String email) {
        return repository.findByUserEmail(email).orElseThrow();
    }


}

