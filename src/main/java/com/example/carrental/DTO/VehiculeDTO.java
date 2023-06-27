package com.example.carrental.DTO;


import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.Categorie;
import com.example.carrental.Enumerations.TypeUtilitaire;
import com.example.carrental.Enumerations.TypeVoiture;
import com.example.carrental.Models.Agence;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class VehiculeDTO {

    private Integer vehiculeId;
    @Size(max = 255)
    private String matricule;
    @Size(max = 255)
    private String nbrplaces;
    @Size(max = 255)
    private String couleur;
    @Size(max = 255)
    private String longueur;
    @Size(max = 255)
    private String largeur;
    @Size(max = 255)
    private String puissance;
    @Size(max = 255)
    private String chargeutile;
    @Size(max = 255)
    private String picture;

    private Integer quantite;

    private boolean isLocked;
    private String description;
    private double prix;

    private Date dateajout;

    private Double jourslocation;

    private Alimentation alimentation;
    private TypeUtilitaire typeUtilitaire;
    private TypeVoiture typeVoiture;
    private Categorie categorie;

    private Agence agence;

}
