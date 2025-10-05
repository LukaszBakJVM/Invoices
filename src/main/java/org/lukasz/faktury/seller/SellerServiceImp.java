package org.lukasz.faktury.seller;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.exceptions.NipAlreadyRegisteredException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Seller save(SellerDto dto) {
        repository.findByNip(dto.nip()).ifPresent(nip -> {
            throw new NipAlreadyRegisteredException(String.format("NIP %s już jest zapisany, można mieć tylko jedno konto", dto.nip()));
        });
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
    private Seller getAuthentication (){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUserEmail(email).orElseThrow();
    }


}

