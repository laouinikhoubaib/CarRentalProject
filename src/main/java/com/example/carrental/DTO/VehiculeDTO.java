package com.example.carrental.DTO;


import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.Categorie;
import com.example.carrental.Enumerations.TypeUtilitaire;
import com.example.carrental.Enumerations.TypeVoiture;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class VehiculeDTO {

    private Integer vehiculeId;

    @Size(max = 255)
    private String matricule;
    @Size(max = 255)
    private String nbr_places;
    @Size(max = 255)
    private String couleur;
    @Size(max = 255)
    private String longueur;
    @Size(max = 255)
    private String largeur;
    @Size(max = 255)
    private String puissance;
    @Size(max = 255)
    private String charge_utile;
    @Size(max = 255)
    private String picture;

    private LocalDate dateajout;

    private Double jourslocation;

    private Alimentation alimentation;
    private TypeUtilitaire typeUtilitaire;
    private TypeVoiture typeVoiture;
    private Categorie categorie;

}
