package com.example.carrental.Repository;



import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.EtatActuel;
import com.example.carrental.Enumerations.TypeCategorie;
import com.example.carrental.Enumerations.TypeVehicule;
import com.example.carrental.Models.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    List<Vehicule> findByAlimentation(Alimentation alimentation);
    List<Vehicule> findByEtatactuel(EtatActuel etatActuel);
    List<Vehicule> findByModelVehiculesTypevehicule(TypeVehicule typeVehicule);
    List<Vehicule> findByModelVehicules_CategorieVehicules_Typecategorie(TypeCategorie typeCategorie);
}

