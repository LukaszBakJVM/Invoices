package org.lukasz.faktury.Buyer;

import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.ceidgapi.ApiConnection;
import org.lukasz.faktury.ceidgapi.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BuyerServiceImpl implements BuyerService {
    private final BuyerMapper buyerMapper;
    private final BuyerRepo buyerRepo;
    private final ApiConnection connection;

    public BuyerServiceImpl(BuyerMapper buyerMapper, BuyerRepo buyerRepo, ApiConnection connection) {
        this.buyerMapper = buyerMapper;
        this.buyerRepo = buyerRepo;
        this.connection = connection;
    }

    @Override
    @Transactional
    public BuyerDto findByNipAndSave(String nip) {
        Optional<Buyer> byNip = buyerRepo.findByNip(nip);
        if (byNip.isPresent()) {
            Buyer buyer = byNip.get();

            return buyerMapper.entityToDto(buyer);
        }
        BuyerDto dataByNip = findDataByNip(nip);
        Buyer entity = buyerMapper.toEntity(dataByNip);
        Buyer save = buyerRepo.save(entity);
        return buyerMapper.entityToDto(save);
    }

    @Override
    public Buyer findBuyer(String nip) {
        return buyerRepo.findByNip(nip).orElseThrow();

    }


    private BuyerDto findDataByNip(String nip) {
        Result result = connection.result(nip).firma().stream().findFirst().orElseThrow();
        return new BuyerDto(result.nazwa(), result.wlasciciel().nip(), result.wlasciciel().regon(), result.adresDzialalnosci().miasto(), result.adresDzialalnosci().kod(), result.adresDzialalnosci().ulica(), result.adresDzialalnosci().budynek());
    }


}


