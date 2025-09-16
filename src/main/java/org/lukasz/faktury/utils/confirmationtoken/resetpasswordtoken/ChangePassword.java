package org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken;

import jakarta.persistence.*;
import org.lukasz.faktury.user.User;

import java.time.LocalDateTime;

@Entity
public class ChangePassword {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime duration;
    private boolean used;
    @OneToOne
   private User user;

    public ChangePassword() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDuration() {
        return duration;
    }

    public void setDuration(LocalDateTime duration) {
        this.duration = duration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
