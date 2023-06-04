//package com.example.carrental.Models;
//
//import com.example.carrental.Enumerations.Categorie;
//import com.example.carrental.Enumerations.TypeVoiture;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@ToString
//@Entity
//@Table(name = "categorievehicule")
//public class CategorieVehicule {
//
//    @Id
//    @Column(nullable = false, updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer categorievehiculeId;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "categorie", nullable = false)
//    Categorie categorie;
//
//
//    @OneToMany(mappedBy = "categorieVehicule", cascade = CascadeType.ALL)
//    private Set<Vehicule> vehicules;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "categorievoiture_id", referencedColumnName = "categorievoitureId")
//    private CategorieVoiture categorieVoiture;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "categorieutilitaire_id", referencedColumnName = "categorieutilitaireId")
//    private CategorieUtilitaire categorieUtilitaire;
//}
