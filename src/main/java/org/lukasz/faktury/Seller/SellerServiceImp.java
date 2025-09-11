package org.lukasz.faktury.Seller;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.exceptions.NipAlreadyRegistered;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImp implements SellerService {
    private final SellerRepo repository;
    private final SellerMapper mapper;

    public SellerServiceImp(SellerRepo repository, SellerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void save(SellerDto dto) {
        repository.findByNip(dto.nip()).ifPresent(nip -> {
            throw new NipAlreadyRegistered(String.format("NIP %s już jest zapisany, można mieć tylko jedno konto", dto.nip()));
        });
        Seller entity = mapper.toEntity(dto);
        repository.save(entity);


    }


}

