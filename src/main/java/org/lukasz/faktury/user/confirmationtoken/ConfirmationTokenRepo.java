package org.lukasz.faktury.user.confirmationtoken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfirmationTokenRepo extends CrudRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken>findByToken(String token);
}
