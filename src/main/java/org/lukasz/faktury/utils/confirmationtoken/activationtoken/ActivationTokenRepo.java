package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.lukasz.faktury.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActivationTokenRepo extends CrudRepository<ActivationToken,Long> {
    Optional<ActivationToken>findByToken(String token);
    Optional<ActivationToken>findByUser(User user);
}
