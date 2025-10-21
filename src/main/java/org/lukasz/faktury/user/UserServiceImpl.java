package org.lukasz.faktury.user;

import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.nipapi.ApiConnection;
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
    public UserResponse register(UserRequest request, SellerDto sellerDto) {
        logger.info("Inside registration");
        validation.validation(request);
        findUserByEmail(request.email());

        Seller seller = sellerService.save(sellerDto);
        logger.info("Inside registration  seller -> {} ", seller);


        User entity = mapper.toEntity(request);



        entity.setActive(false);
        entity.setNip(request.nip());
        entity.setSeller(seller);
        User save = repository.save(entity);
        logger.info("Inside registration  user -> {} ", entity);
        activationTokenService.createToken(save);

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

    @Override
    public List<SellerDto> findDataByNip(String nip) {
        return connection.result(nip).firma().stream().map(r -> new SellerDto(r.nazwa(), r.wlasciciel().nip(), r.wlasciciel().regon(), r.adresDzialalnosci().miasto(), r.adresDzialalnosci().kod(), r.adresDzialalnosci().ulica(), r.adresDzialalnosci().budynek())).toList();


    }


}










