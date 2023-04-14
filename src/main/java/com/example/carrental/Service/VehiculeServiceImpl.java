package com.example.carrental.Service;


import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.EtatActuel;
import com.example.carrental.Enumerations.TypeCategorie;
import com.example.carrental.Enumerations.TypeVehicule;
import com.example.carrental.Models.*;
import com.example.carrental.Repository.CategorieVehiculeRepository;
import com.example.carrental.Repository.ModelVehiculeRepository;
import com.example.carrental.Repository.NotificationRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.ServiceInterfaces.VehiculeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class VehiculeServiceImpl implements VehiculeService {


    @Autowired
    VehiculeRepository vehiculeRepository;

    @Autowired
    private ModelVehiculeRepository modelVehiculeRepository;

    @Autowired
    CategorieVehiculeRepository categorieVehiculeRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Inject
    private EntityManager entityManager;
    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, ModelVehiculeRepository modelVehiculeRepository, CategorieVehiculeRepository categorieVehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.modelVehiculeRepository = modelVehiculeRepository;
        this.categorieVehiculeRepository = categorieVehiculeRepository;
    }

    @Override
    public Vehicule addVehicule(Vehicule vehicule, String agenceName) {

        TypedQuery<Agence> query = entityManager.createQuery("SELECT a FROM Agence a WHERE a.nom = :nomAgence", Agence.class);
        query.setParameter("nomAgence", agenceName);
        List<Agence> agences = query.getResultList();
        Agence agence = agences.get(0);
        vehicule.setAgence(agence);
        Notification notif = new Notification();
        notif.setCreatedAt(new Date());
        notif.setMessage("Une nouvelle vehicule a été ajoutée avec succès!");
        notif.setRead(false);
        notif.setVehicule(vehicule);
        notificationRepository.save(notif);
        return vehiculeRepository.save(vehicule);

    }

    @Override
    public List<Vehicule> findAllAgences() {
        List<Vehicule> vehicules = new ArrayList<>();
        vehiculeRepository.findAll().forEach(vehicules::add);
        return vehicules;
    }

    @Override
    public Vehicule getVehiculeById(Long vehiculeId) {
        return vehiculeRepository.findById(vehiculeId).get();
    }

    @Override
    public Vehicule updateVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public void deleteVehicule(Long vehiculeId) {
        vehiculeRepository.deleteById(vehiculeId);
    }

    @Override
    public List<Vehicule> findVehiculeByAlimentation(Alimentation alimentation) {
        return vehiculeRepository.findByAlimentation(alimentation);
    }

    @Override
    public List<Vehicule> findVehiculeByEtatActuel(EtatActuel etatActuel) {
        return vehiculeRepository.findByEtatactuel(etatActuel);
    }

    @Override
    public List<Vehicule> findVehiculeByTypeVehicule(TypeVehicule typeVehicule) {
        return vehiculeRepository.findByModelVehiculesTypevehicule(typeVehicule);
    }
    @Override
    public List<Vehicule> findByTypeCategorie(TypeCategorie typeCategorie) {
        return vehiculeRepository.findByModelVehicules_CategorieVehicules_Typecategorie(typeCategorie);
    }

    //////////////////////////////////////////////////////////////////////////
    /////////////////Model Vehicule//////////////////////////////////////////

    @Override
    public ModelVehicule ajouterModelVehicule(ModelVehicule modelVehicule) {
        return modelVehiculeRepository.save(modelVehicule);
    }

    @Override
    public ModelVehicule updateModelVehicule(Long id, ModelVehicule modelVehicule) {
        ModelVehicule modelVehiculeToUpdate = modelVehiculeRepository.findById(id).orElseThrow(() -> new RuntimeException("ModelVehicule introuvable"));
        modelVehiculeToUpdate.setTypevehicule(modelVehicule.getTypevehicule());
        return modelVehiculeRepository.save(modelVehiculeToUpdate);
    }
    @Override
    public void deleteModelVehicule(Long modelId) {
        modelVehiculeRepository.deleteById(modelId);
    }

    @Override
    public List<ModelVehicule> findAllModelVehicule() {
        return modelVehiculeRepository.findAll();
    }


    ////////////////////////////////////////////////////////////////////////
    ///////////////////////Categorie Vehicule//////////////////////////////

    @Override
    public CategorieVehicule ajouterCategorieVehicule(CategorieVehicule categorieVehicule) {
        return categorieVehiculeRepository.save(categorieVehicule);
    }
    @Override
    public CategorieVehicule updateCategorieVehicule(Long categorieId, CategorieVehicule categorieVehicule) {
        Optional<CategorieVehicule> optionalCategorieVehicule = categorieVehiculeRepository.findById(categorieId);
        if (optionalCategorieVehicule.isPresent()) {
            CategorieVehicule existingCategorieVehicule = optionalCategorieVehicule.get();
            existingCategorieVehicule.setTypecategorie(categorieVehicule.getTypecategorie());
            existingCategorieVehicule.setModelvehicule(categorieVehicule.getModelvehicule());
            return categorieVehiculeRepository.save(existingCategorieVehicule);
        } else {
            throw new EntityNotFoundException("Categorie de véhicule avec l'identifiant " + categorieId + " n'existe pas !");
        }
    }
    @Override
    public CategorieVehicule findCategorieVehiculeById(Long categorieId) {
        return categorieVehiculeRepository.findById(categorieId).orElseThrow(() -> new NotFoundException("CategorieVehicule"+ categorieId));
    }

    @Override
    public void deleteCategorieVehicule(Long categorieId) {
        categorieVehiculeRepository.deleteById(categorieId);
    }

    @Override
    public ModelVehicule findById(Long modelId) {
        Optional<ModelVehicule> modelVehicule = modelVehiculeRepository.findById(modelId);
        if (modelVehicule.isPresent()) {
            return modelVehicule.get();
        } else {
            throw new RuntimeException("ModelVehicule not found with id " + modelId);
        }
    }
}
