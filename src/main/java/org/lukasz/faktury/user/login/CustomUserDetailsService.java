package org.lukasz.faktury.user.login;

import org.lukasz.faktury.user.UserService;
import org.lukasz.faktury.user.dto.Login;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Login login = userService.login(username);
        return createUserDetails(login);

    }

    private UserDetails createUserDetails(Login login) {
        return User.builder().username(login.username()).password(login.password()).build();
    }

}
