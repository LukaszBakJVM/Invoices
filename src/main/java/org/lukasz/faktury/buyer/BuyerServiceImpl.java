package org.lukasz.faktury.buyer;

import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.exceptions.BuyerNotFoundException;
import org.lukasz.faktury.nipapi.ApiConnection;
import org.lukasz.faktury.nipapi.ceidgapi.CeidgResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        List<Buyer> byNip = buyerRepo.findByNip(nip);
        if (!byNip.isEmpty()) {
            Buyer buyer = byNip.getLast();
            return buyerMapper.entityToDto(buyer);
        }
        BuyerDto dataByNip = findDataByNip(nip);

        Buyer entity = buyerMapper.toEntity(dataByNip);
        Buyer save = buyerRepo.save(entity);
        return buyerMapper.entityToDto(save);
    }

    @Override
    public Buyer findBuyer(BuyerDto buyerDto) {
        return buyerRepo.findByNipAndName(buyerDto.nip(), buyerDto.name()).orElseThrow();

    }

    @Override
    public void findByNipAndNameAndSave(BuyerDto buyerDto) {
        buyerRepo.findByNipAndName(buyerDto.nip(), buyerDto.name()).ifPresentOrElse(buyer -> {},
                () -> buyerRepo.save(buyerMapper.toEntity(buyerDto)));
    }

    @Override
    public BuyerDto findByNipAndName(String nip, String companyName) {
        Buyer buyer = buyerRepo.findByNipAndName(nip, companyName).orElseThrow(() -> new BuyerNotFoundException("Błąd Firmy , Zapisz ponownie firmę "));

        return buyerMapper.entityToDto(buyer);

    }


    private BuyerDto findDataByNip(String nip) {
        CeidgResult ceidgResult = connection.result(nip).firma().stream().findFirst().orElseThrow();
        return new BuyerDto(ceidgResult.nazwa(), ceidgResult.wlasciciel().nip(), ceidgResult.wlasciciel().regon(), ceidgResult.adresDzialalnosci().miasto(), ceidgResult.adresDzialalnosci().kod(), ceidgResult.adresDzialalnosci().ulica(), ceidgResult.adresDzialalnosci().budynek());
    }


}


