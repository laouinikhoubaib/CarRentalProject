package com.example.carrental.Models;


import com.example.carrental.Enumerations.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
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

    @Column
    private double prix;

    @Column(name = "\"description\"")
    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateajout;


    @Column(name = "Picture")
    String picture;

    @OneToOne
    Media pictures;

    @Column
    boolean isLocked;

    @Column
    private Double jourslocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "alimentation")
    Alimentation alimentation;


    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private Categorie categorie;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_voiture")
    private TypeVoiture typeVoiture;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_utilitaire")
    private TypeUtilitaire typeUtilitaire;



    @JsonIgnore
    @OneToMany(mappedBy = "vehiculeReservation")
    private Set<Reservation> vehiculeReservationReservations;


    @JsonIgnore
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
