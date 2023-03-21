package com.example.carrental.Models;


import com.example.carrental.Enumerations.NomAgence;
import com.example.carrental.Enumerations.TypeAgence;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "agence")
public class Agence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long agenceId;

    @Column(name = "numeroAgence", nullable = false)
    Integer numeroAgence;



    @Enumerated(EnumType.STRING)
    @Column(name = "nomAgence", nullable = false)
    NomAgence nomAgence;


    @Enumerated(EnumType.STRING)
    @Column(name = "typeagence", nullable = false)
    TypeAgence typeagence;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agence")
    Set<User> users;

    public Agence(Integer numeroAgence, NomAgence nomAgence, TypeAgence typeagence, Set<User> users) {
        this.numeroAgence = numeroAgence;
        this.nomAgence = nomAgence;
        this.typeagence = typeagence;
        this.users = users;
    }

    public Agence(Long agenceId) {
        this.agenceId = agenceId;
    }
}
