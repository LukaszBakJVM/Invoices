package org.lukasz.faktury.user;

import jakarta.persistence.*;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationToken;
import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.ChangePassword;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private  String nip;
    private boolean  active;
    @OneToOne()
    private Seller seller;
    @OneToMany(mappedBy = "user")

    private List<ActivationToken> activationTokens;
    @OneToOne

    private ChangePassword changePassword;


    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<ActivationToken> getActivationTokens() {
        return activationTokens;
    }

    public void setActivationTokens(List<ActivationToken> activationTokens) {
        this.activationTokens = activationTokens;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public List<ActivationToken> getConfirmationTokens() {
        return activationTokens;
    }

    public void setConfirmationTokens(List<ActivationToken> activationTokens) {
        this.activationTokens = activationTokens;
    }

    public ChangePassword getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(ChangePassword changePassword) {
        this.changePassword = changePassword;
    }
}
