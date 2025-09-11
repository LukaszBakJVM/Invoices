package org.lukasz.faktury.user;

import org.lukasz.faktury.gusapi.ApiConnection;
import org.lukasz.faktury.gusapi.NipApiResponse;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.lukasz.faktury.utils.Validation;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final Validation validation;
    private final ApiConnection connection;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, Validation validation, ApiConnection connection) {
        this.repository = repository;
        this.mapper = mapper;
        this.validation = validation;
        this.connection = connection;
    }
  public   UserResponse register(UserRequest request){
        validation.validation(request);
       // NipApiResponse result = connection.result(request.nip());

        //TODO zapisz dane firmy do bazy
        Registration entity = mapper.toEntity(request);
        entity.setActive(false);
      Registration save = repository.save(entity);
      return mapper.toResponse(save);


    }
}
