package org.lukasz.faktury.user.confirmationtoken;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.exceptions.TokenException;
import org.lukasz.faktury.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepo tokenRepository;
    private final EmailSender emailSender;
    @Value("${tokenUrl}")
    private String tokenUrl;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepo repo, EmailSender emailSender) {
        this.tokenRepository = repo;
        this.emailSender = emailSender;
    }

    @Override
    public void createToken(User user) {
        ConfirmationToken token = new ConfirmationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        token.setUser(user);
        ConfirmationToken generatedToken = tokenRepository.save(token);
        String link = link(generatedToken.getToken());
        emailSender.sendEmail(link);


    }

    @Override
    @Transactional
    public void findToken(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token).orElseThrow(() -> new TokenException("Token nie istnieje"));
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        boolean used = confirmationToken.isUsed();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenException("Token wygasł");
        } else if (used) {
            throw new TokenException("Token już wykorzystany");
        }
        confirmationToken.setUsed(true);
        User user = confirmationToken.getUser();
        user.setActive(true);
        tokenRepository.save(confirmationToken);


    }

    private String link(final String token) {
        return UriComponentsBuilder.fromUriString(tokenUrl + "/confirm?token={token}").buildAndExpand(token).toUriString();
    }


}
