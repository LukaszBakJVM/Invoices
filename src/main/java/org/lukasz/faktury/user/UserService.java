package org.lukasz.faktury.user;

import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;

public interface UserService {
    UserResponse register(UserRequest request);

}
