package com.example.carrental.Models;


import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehiculeId;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "nbr_places")
    private String nbrplaces;

    @Column(name = "couleur")
    private String couleur;

    @Column(name = "longueur")
    private String longueur;

    @Column(name = "largeur")
    private String largeur;

    @Column(name = "puissance")
    private String puissance;

    @Column(name = "charge_utile")
    private String chargeutile;


    @Column(name = "\"description\"")
    private String description;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateajout;


    @Column(name = "Picture")
    String picture;

    @OneToOne
    Media pictures;

    @Column
    private Double jourslocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "alimentation", nullable = false)
    Alimentation alimentation;

    @OneToMany(mappedBy = "vehiculeCategorie")
    private Set<CategorieVehicule> vehiculeCategorievehiculeCategories;


    @OneToMany(mappedBy = "vehiculeReservation")
    private Set<Reservation> vehiculeReservationReservations;


    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id", referencedColumnName = "agenceId")
    private Agence agence;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehicule")
    @JsonIgnore
    Set<Notification> notifications;
}
