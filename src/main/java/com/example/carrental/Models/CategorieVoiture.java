//package com.example.carrental.Models;
//
//
//import com.example.carrental.Enumerations.Alimentation;
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
//@Table(name = "categorievoiture")
//public class CategorieVoiture {
//
//
//    @Id
//    @Column(nullable = false, updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer categorievoitureId;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "typevoiture", nullable = false)
//    TypeVoiture typeVoiture;
//
//    @OneToMany(mappedBy = "categorieVoiture", cascade = CascadeType.ALL)
//    private Set<CategorieVehicule> categorieVehicules;
//
//}
