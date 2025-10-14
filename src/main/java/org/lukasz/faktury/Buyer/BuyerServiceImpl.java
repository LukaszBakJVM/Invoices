package org.lukasz.faktury.Buyer;

import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.nipapi.ApiConnection;
import org.lukasz.faktury.nipapi.ceidgapi.CeidgResult;
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
        CeidgResult ceidgResult = connection.result(nip).firma().stream().findFirst().orElseThrow();
        return new BuyerDto(ceidgResult.nazwa(), ceidgResult.wlasciciel().nip(), ceidgResult.wlasciciel().regon(), ceidgResult.adresDzialalnosci().miasto(), ceidgResult.adresDzialalnosci().kod(), ceidgResult.adresDzialalnosci().ulica(), ceidgResult.adresDzialalnosci().budynek());
    }


}


