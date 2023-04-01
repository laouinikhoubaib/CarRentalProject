package com.example.carrental.Models;


import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.EtatActuel;
import com.example.carrental.Enumerations.TypeCategorie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "vehicule")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long vehiculeId;

    @Column(name = "matricule",  nullable = false, length = 100)
    String matricule;

    @Column(name = "nbr_places", nullable = false, length = 100)
    String nbr_places;

    @Column(name = "couleur", nullable = false, length = 100)
    String couleur;

    @Column(name = "longueur", nullable = false, length = 100)
    String longueur;

    @Column(name = "largeur",  nullable = false, length = 100)
    String largeur;

    @Column(name = "puissance", nullable = false, length = 100)
    String puissance;

    @Column(name = "charge_utile", nullable = false, length = 100)
    String charge_utile;

    @Enumerated(EnumType.STRING)
    Alimentation alimentation;

    @Enumerated(EnumType.STRING)
    EtatActuel etatactuel;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "vehicule")
    Set<ModelVehicule> modelVehicules;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

}
