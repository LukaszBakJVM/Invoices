package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChangePasswordRepo extends CrudRepository<ChangePassword, Long> {
    Optional<ChangePassword>findByUserEmail(String email);
    Optional<ChangePassword>findByTokenAndUserEmail(String token,String email);
}
