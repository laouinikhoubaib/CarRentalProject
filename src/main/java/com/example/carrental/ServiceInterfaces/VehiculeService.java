package com.example.carrental.ServiceInterfaces;

import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.EtatActuel;
import com.example.carrental.Enumerations.TypeCategorie;
import com.example.carrental.Enumerations.TypeVehicule;
import com.example.carrental.Models.CategorieVehicule;
import com.example.carrental.Models.ModelVehicule;
import com.example.carrental.Models.Vehicule;

import java.util.List;

public interface VehiculeService {


    public Vehicule addVehicule(Vehicule vehicule, String agenceName);

    List<Vehicule> findAllAgences();

    Vehicule getVehiculeById(Long vehiculeId);

    public Vehicule updateVehicule(Vehicule vehicule);

    void deleteVehicule(Long vehiculeId);

    public List<Vehicule> findVehiculeByAlimentation(Alimentation alimentation);

    public List<Vehicule> findVehiculeByEtatActuel(EtatActuel etatActuel);

    public List<Vehicule> findVehiculeByTypeVehicule(TypeVehicule typeVehicule);

    public List<Vehicule> findByTypeCategorie(TypeCategorie typeCategorie);


    public ModelVehicule ajouterModelVehicule(ModelVehicule modelVehicule);

    public ModelVehicule updateModelVehicule(Long id, ModelVehicule modelVehicule);
    public void deleteModelVehicule(Long modelId);

    public List<ModelVehicule> findAllModelVehicule();

    public CategorieVehicule ajouterCategorieVehicule(CategorieVehicule categorieVehicule);

    public CategorieVehicule updateCategorieVehicule(Long categorieId, CategorieVehicule categorieVehicule);

    public CategorieVehicule findCategorieVehiculeById(Long categorieId);

    public void deleteCategorieVehicule(Long categorieId);

    public ModelVehicule findById(Long modelId);

}
