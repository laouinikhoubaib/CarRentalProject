package com.example.carrental.Models;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

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

    @Column(name = "matricule", unique = true, nullable = false, length = 100)
    String matricule;

    @Column(name = "nbr_places", unique = true, nullable = false, length = 100)
    String nbr_places;

    @Column(name = "couleur", unique = true, nullable = false, length = 100)
    String couleur;

    @Column(name = "longueur", unique = true, nullable = false, length = 100)
    String longueur;

    @Column(name = "largeur", unique = true, nullable = false, length = 100)
    String largeur;

    @Column(name = "puissance", unique = true, nullable = false, length = 100)
    String puissance;
}
