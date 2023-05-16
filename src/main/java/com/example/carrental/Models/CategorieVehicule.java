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
@Table(name = "CategorieVehicule")
public class CategorieVehicule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long categorieutilitaireId;


    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "utilitaire")
    private LocationUtilitaire locationUtilitaire;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "voiture")
    private LocationVoiture locationVoiture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_catveh_categorie_id")
    private Vehicule vehiculeCategorie;
}
