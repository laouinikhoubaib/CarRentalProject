package com.example.carrental.Models;



import com.example.carrental.Enumerations.LocationVoitureEn;
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
@Table(name = "CategorieVehiculeVoiture")
public class LocationVoiture {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voitureId;


    @Enumerated(EnumType.STRING)
    LocationVoitureEn locationVoitures;


    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "voitureCatevehicule")
    private CategorieVehicule voiture;

}
