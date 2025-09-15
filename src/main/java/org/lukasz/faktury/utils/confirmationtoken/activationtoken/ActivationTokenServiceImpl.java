package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.exceptions.TokenException;
import org.lukasz.faktury.user.User;
import org.lukasz.faktury.utils.confirmationtoken.EmailSenderService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service

public class ActivationTokenServiceImpl implements ActivationTokenService {
    private final ActivationTokenRepo tokenRepository;
    private final EmailSenderService emailSenderService;
    @Value("${tokenUrl}")
    private String tokenUrl;

    public ActivationTokenServiceImpl(ActivationTokenRepo repo,@Qualifier("accountActivation") EmailSenderService emailSenderService) {
        this.tokenRepository = repo;

        this.emailSenderService = emailSenderService;
    }

    @Override
    public void createToken(User user) {
        ActivationToken token = new ActivationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        token.setUser(user);
        ActivationToken generatedToken = tokenRepository.save(token);
        String link = link(generatedToken.getToken());
        emailSenderService.sendEmail(user.getEmail(),link);


    }

    @Override
    @Transactional
    public void findToken(String token) {
        ActivationToken activationToken = tokenRepository.findByToken(token).orElseThrow(() -> new TokenException("Token nie istnieje"));
        LocalDateTime expiresAt = activationToken.getExpiresAt();
        boolean used = activationToken.isUsed();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenException("Token wygasł");
        } else if (used) {
            throw new TokenException("Token już wykorzystany");
        }
        activationToken.setUsed(true);
        User user = activationToken.getUser();
        user.setActive(true);
        tokenRepository.save(activationToken);


    }

    private String link(final String token) {
        return UriComponentsBuilder.fromUriString(tokenUrl + "/confirm?token={token}").buildAndExpand(token).toUriString();
    }


}
