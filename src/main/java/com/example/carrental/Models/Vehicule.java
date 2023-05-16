package com.example.carrental.Models;


import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.EtatActuel;
import com.example.carrental.Enumerations.TypeCategorie;
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

    @Column(name = "matricule",  nullable = false, length = 100)
    private String matricule;

    @Column(name = "nbr_places", nullable = false, length = 100)
    private String nbr_places;

    @Column(name = "couleur", nullable = false, length = 100)
    private String couleur;

    @Column(name = "longueur", nullable = false, length = 100)
    private String longueur;

    @Column(name = "largeur",  nullable = false, length = 100)
    private String largeur;

    @Column(name = "puissance", nullable = false, length = 100)
    private String puissance;

    @Column(name = "charge_utile", nullable = false, length = 100)
    private String charge_utile;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateajout;

    @Column
    private String picture;

    @Column
    private Double jourslocation;


    @Enumerated(EnumType.STRING)
    private Alimentation alimentation;

    @Enumerated(EnumType.STRING)
    private EtatActuel etatactuel;



    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "vehicule")
    Set<ModelVehicule> modelVehicules;

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
