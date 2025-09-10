package org.lukasz.faktury.user;

import jakarta.persistence.*;
import org.lukasz.faktury.Seller.Seller;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @OneToOne
    private Seller seller;

    public Users() {
    }
}
