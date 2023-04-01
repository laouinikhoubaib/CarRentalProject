package com.example.carrental.Models;


import com.example.carrental.Enumerations.TypeAgence;
import com.example.carrental.Enumerations.TypeVehicule;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "ModeVehicule")
public class ModelVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long modelId;


    @Enumerated(EnumType.STRING)
    TypeVehicule typevehicule;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id", referencedColumnName = "vehiculeId")
    private Vehicule vehicule;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "modelvehicule")
    Set<CategorieVehicule> categorieVehicules;


}
