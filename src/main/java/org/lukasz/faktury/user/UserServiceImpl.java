package org.lukasz.faktury.user;

import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.gusapi.ApiConnection;
import org.lukasz.faktury.gusapi.Subject;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.lukasz.faktury.utils.Validation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final Validation validation;
    private final ApiConnection connection;
    private final SellerService sellerService;


    public UserServiceImpl(UserRepository repository, UserMapper mapper, Validation validation, ApiConnection connection, SellerService sellerService) {
        this.repository = repository;
        this.mapper = mapper;
        this.validation = validation;
        this.connection = connection;
        this.sellerService = sellerService;

    }


    public UserResponse register(UserRequest request) {
        validation.validation(request);
        SellerDto dataByNip = findDataByNip(request.nip());
        Seller seller = sellerService.save(dataByNip);


        Registration entity = mapper.toEntity(request);

        entity.setActive(false);
        entity.setNip(request.nip());
        entity.setSeller(seller);
        Registration save = repository.save(entity);
        return mapper.toResponse(save);
    }

    private SellerDto findDataByNip(String nip) {
        Subject subject = connection.result(nip).result().subject();
        Address address;
        if (subject.workingAddress() != null) {
            address = address(subject.workingAddress());
        } else {
            address = address(subject.residenceAddress());

        }
        return new SellerDto(subject.name(), subject.nip(), subject.regon(), address.city(), address.zipcode(), address.street(), address.houseNumber());
    }

    Address address(String workingAddress) {
        List<String> data = Arrays.stream(workingAddress.split("[ ,]+")).toList();

        return new Address(data.get(2), data.get(3), data.get(0), data.get(1));

    }

}

record Address(String zipcode, String city, String street, String houseNumber) {
}








