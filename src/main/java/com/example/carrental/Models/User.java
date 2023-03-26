package com.example.carrental.Models;


import com.example.carrental.Enumerations.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "isLocked", nullable = false)
    boolean isLocked;

    @Column(name = "loginAttempts", nullable = false)
    int loginAttempts;

    @Column(name = "profilPic", nullable = false)
    String profilPic;

    @Temporal(TemporalType.DATE)
    Date registrationDate;

    @Transient
    String accessToken;

    @Transient
    String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    Role role;

    @OneToOne
    Media profilPicture;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    Set<Notification> notifications;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id", referencedColumnName = "agenceId")
    private Agence agence;
}