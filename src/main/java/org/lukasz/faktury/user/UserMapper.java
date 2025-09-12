package org.lukasz.faktury.user;

import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    User toEntity(UserRequest request) {
        User user = new User();

        user.setPassword(request.password());
        user.setEmail(request.email());
        return user;
    }

    UserResponse toResponse(User response) {
        return new UserResponse(response.getEmail());
    }
}


