//package com.example.carrental.Models;
//
//
//import com.example.carrental.Enumerations.TypeUtilitaire;
//import com.example.carrental.Enumerations.TypeVoiture;
//import com.fasterxml.jackson.annotation.JsonIgnore;
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
//@Table(name = "categorieutilitaire")
//public class CategorieUtilitaire {
//
//    @Id
//    @Column(nullable = false, updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer categorieutilitaireId;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "typeutilitaire", nullable = false)
//    TypeUtilitaire typeUtilitaire;
//
//
//    @OneToMany(mappedBy = "categorieUtilitaire", cascade = CascadeType.ALL)
//    private Set<CategorieVehicule> categorieVehicules;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "categorieVoiture", cascade = CascadeType.ALL)
//    private Set<Vehicule> vehicules;
//
//}
