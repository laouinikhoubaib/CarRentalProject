package com.example.carrental.Models;


import com.example.carrental.Enumerations.TypeCategorie;
import com.example.carrental.Enumerations.TypeVehicule;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    Long categorieId;

    @Enumerated(EnumType.STRING)
    TypeCategorie typecategorie;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", referencedColumnName = "modelId")
    private ModelVehicule modelvehicule;
}
