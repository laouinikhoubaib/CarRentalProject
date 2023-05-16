package com.example.carrental.Models;


import com.example.carrental.Enumerations.LocationUtilitaireEn;
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
@Table(name = "CategorieVehiculeUtilitaire")
public class LocationUtilitaire {


    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer utilitaireId;


    @Enumerated(EnumType.STRING)
    LocationUtilitaireEn locationUtilitaire;


    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "utilitaireCatevehicule")
    private CategorieVehicule utilitaire;

}
