package org.lukasz.faktury.user;

import org.lukasz.faktury.exceptions.NipAlreadyRegisteredException;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.gusapi.ApiConnection;
import org.lukasz.faktury.gusapi.Subject;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.user.dto.Login;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationTokenService;
import org.lukasz.faktury.utils.validation.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final Validation validation;
    private final ApiConnection connection;
    private final SellerService sellerService;
    private final ActivationTokenService activationTokenService;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    public UserServiceImpl(UserRepository repository, UserMapper mapper, Validation validation, ApiConnection connection, SellerService sellerService, ActivationTokenService activationTokenService) {
        this.repository = repository;
        this.mapper = mapper;
        this.validation = validation;
        this.connection = connection;
        this.sellerService = sellerService;
        this.activationTokenService = activationTokenService;
    }

    @Override
    @Transactional
    public UserResponse register(UserRequest request) {
        logger.info("Inside registration");
        validation.validation(request);
        findUserByEmail(request.email());
        SellerDto dataByNip = findDataByNip(request.nip());
        logger.info("Inside registration  sellerDto -> {} ", dataByNip);
        Seller seller = sellerService.save(dataByNip);


        User entity = mapper.toEntity(request);


        entity.setActive(false);
        entity.setNip(request.nip());
        entity.setSeller(seller);
        User save = repository.save(entity);
        activationTokenService.createToken(save);
        logger.info("Inside registration  sellerDto -> {} ", dataByNip);
        return mapper.toResponse(save);
    }

    @Override
    public Login login(String email) {
        User login = repository.findByEmail(email).orElseThrow(() -> new UserException("Niepoprawny email"));
        if (!login.isActive()) {
            throw new UserException("Aktywuj konto");
        }
        return new Login(login.getEmail(), login.getPassword());


    }

    private void findUserByEmail(String email) {
        repository.findByEmail(email).ifPresent(present -> {
            try {
                throw new LoginException("Użytkownik już posiada konto");
            } catch (LoginException e) {
                throw new UserException(e.getMessage());
            }
        });



    }

    private SellerDto findDataByNip(String nip) {
        repository.findByNip(nip).ifPresent(present -> {
            throw new NipAlreadyRegisteredException(String.format("NIP %s już jest zapisany, można mieć tylko jedno konto", nip));
        });

        Subject subject = connection.result(nip).result().subject();
        Address address;
        if (subject.workingAddress() != null) {
            address = address(subject.workingAddress());
        } else {
            address = address(subject.residenceAddress());

        }
        return new SellerDto(subject.name(), subject.nip(), subject.regon(), address.city(), address.zipcode(), address.street(), address.houseNumber());
    }

    private Address address(String workingAddress) {
        List<String> data = Arrays.stream(workingAddress.split("[ ,]+")).toList();

        return new Address(data.get(2), data.get(3), data.get(0), data.get(1));

    }

}

record Address(String zipcode, String city, String street, String houseNumber) {
}








