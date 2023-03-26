package com.example.carrental.Models;


import com.example.carrental.Enumerations.TypeAgence;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "numero", nullable = false)
    String numero;

    @Column(name = "nom", nullable = false)
    String nom;

    @Enumerated(EnumType.STRING)
    TypeAgence typeagence;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "agence")
    Set<User> users;

    public Agence(Long agenceId) {
        this.agenceId = agenceId;
    }
}