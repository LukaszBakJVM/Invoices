package org.lukasz.faktury.user;

import org.lukasz.faktury.seller.dto.SellerDto;
import org.lukasz.faktury.user.dto.Login;
import org.lukasz.faktury.user.dto.RegisterDto;
import org.lukasz.faktury.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse register(RegisterDto register);
    Login login(String email);

    List<SellerDto> findDataByNip(String nip);



}
