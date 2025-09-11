package org.lukasz.faktury.user;

import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    Registration toEntity(UserRequest request) {
        Registration registration = new Registration();
        registration.setUsername(request.username());
        registration.setPassword(request.password());
        registration.setEmail(request.email());
        return registration;
    }

    UserResponse toResponse(Registration response) {
        return new UserResponse(response.getUsername(), response.getEmail());
    }
}


