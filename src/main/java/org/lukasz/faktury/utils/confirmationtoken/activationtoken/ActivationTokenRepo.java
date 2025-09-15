package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActivationTokenRepo extends CrudRepository<ActivationToken,Long> {
    Optional<ActivationToken>findByToken(String token);
}
