package com.example.carrental.Models;


import com.example.carrental.Enumerations.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "users")
public class User implements Serializable {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    String username;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    String name;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "PhoneNumber", nullable = false)
    String PhoneNumber;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "block", nullable = false)
    boolean block;

    @Column(name = "loginAttempts", nullable = false)
    int loginAttempts;

    @Column(name = "profilPic", nullable = false)
    String profilPic;

    @Transient
    String accessToken;

    @Transient
    String refreshToken;


    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    Role role;


    @OneToOne
    Media profilPicture;


}
