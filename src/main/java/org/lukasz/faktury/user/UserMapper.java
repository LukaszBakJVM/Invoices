package org.lukasz.faktury.user;

import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    User toEntity(UserRequest request) {
        User user = new User();
        user.setEmail(request.email());
        String password = passwordEncoder.encode(request.password());


        user.setPassword(password);

        return user;
    }

    UserResponse toResponse(User response) {
        return new UserResponse(response.getEmail());
    }
}


