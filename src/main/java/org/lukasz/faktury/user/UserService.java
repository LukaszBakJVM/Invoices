package org.lukasz.faktury.user;

import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.user.dto.Login;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse register(UserRequest request, SellerDto sellerDto);
    Login login(String email);

    List<SellerDto> findDataByNip(String nip);



}
