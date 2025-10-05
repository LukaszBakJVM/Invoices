package org.lukasz.faktury.Buyer;

import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.gusapi.ApiConnection;
import org.lukasz.faktury.gusapi.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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
        Subject subject = connection.result(nip).result().subject();
        Address address;
        if (subject.workingAddress() != null) {
            address = address(subject.workingAddress());
        } else {
            address = address(subject.residenceAddress());

        }
        return new BuyerDto(subject.name(), subject.nip(), subject.regon(), address.city(), address.zipcode(), address.street(), address.houseNumber());
    }

    private Address address(String workingAddress) {
        List<String> data = Arrays.stream(workingAddress.split("[ ,]+")).toList();

        return new Address(data.get(2), data.get(3), data.get(0), data.get(1));

    }

}

record Address(String zipcode, String city, String street, String houseNumber) {
}

