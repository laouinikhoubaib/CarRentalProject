package com.example.carrental.Controllers;


import com.example.carrental.Enumerations.Alimentation;
import com.example.carrental.Enumerations.EtatActuel;
import com.example.carrental.Enumerations.TypeCategorie;
import com.example.carrental.Enumerations.TypeVehicule;
import com.example.carrental.Models.CategorieVehicule;
import com.example.carrental.Models.ModelVehicule;
import com.example.carrental.Models.Vehicule;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.ServiceInterfaces.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/vehicule")
@CrossOrigin(origins = "http://localhost:4200")
public class VehiculeController {

    @Autowired
    VehiculeRepository vehiculeRepository;

    @Autowired
    VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }


    @PostMapping("/addVehicule")
    @ResponseBody
    public Vehicule addVehicule(@RequestBody Vehicule vehicule){
        return vehiculeService.addVehicule(vehicule);

    }

    @PutMapping({"/updateVehicule"})
    public Vehicule updateAgence(@RequestBody Vehicule vehicule) {
        return vehiculeService.updateVehicule(vehicule);

    }

    @DeleteMapping({"/deleteVehicule/{vehiculeId}"})
    @ResponseBody
    public ResponseEntity<Vehicule> deleteVehicule(@PathVariable("vehiculeId") Long vehiculeId) {
        vehiculeService.deleteVehicule(vehiculeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping({"/getVehicule/{vehiculeId}"})
    public ResponseEntity<Vehicule> getVehicule(@PathVariable Long vehiculeId) {
        return new ResponseEntity<>(vehiculeService.getVehiculeById(vehiculeId), HttpStatus.OK);
    }

    @GetMapping("/allvehicules")
    public List<Vehicule> findAllVehicules()
    {
        return vehiculeService.findAllAgences();
    }

    @GetMapping("/alimentation/{alimentation}")
    public ResponseEntity<List<Vehicule>> findVehiculeByAlimentation(@PathVariable Alimentation alimentation) {
        List<Vehicule> vehicules = vehiculeService.findVehiculeByAlimentation(alimentation);
        return ResponseEntity.ok().body(vehicules);
    }

    @GetMapping("/byetatactuel/{etatActuel}")
    public List<Vehicule> findVehiculeByEtatActuel(@PathVariable EtatActuel etatActuel) {
        return vehiculeService.findVehiculeByEtatActuel(etatActuel);
    }

    @GetMapping("/type/{typeVehicule}")
    public ResponseEntity<List<Vehicule>> findVehiculeByTypeVehicule(@PathVariable TypeVehicule typeVehicule) {
        List<Vehicule> vehicules = vehiculeService.findVehiculeByTypeVehicule(typeVehicule);

        if (vehicules.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(vehicules);
        }
    }

    @GetMapping("/categorie/{typeCategorie}")
    public List<Vehicule> findVehiculeByTypeCategorie(@PathVariable TypeCategorie typeCategorie) {
        return vehiculeService.findByTypeCategorie(typeCategorie);
    }

    @PostMapping("/addmodelVehicule")
    public ResponseEntity<ModelVehicule> ajouterModelVehicule(@RequestBody ModelVehicule modelVehicule) {
        ModelVehicule modelVehiculeAjoute = vehiculeService.ajouterModelVehicule(modelVehicule);
        return ResponseEntity.ok().body(modelVehiculeAjoute);
    }

    @PutMapping("/updatemodelVehicule/{id}")
    public ResponseEntity<ModelVehicule> updateModelVehicule(@PathVariable(value = "id") Long id, @RequestBody ModelVehicule modelVehicule) {
        ModelVehicule modelVehiculeUpdate = vehiculeService.updateModelVehicule(id, modelVehicule);
        return ResponseEntity.ok().body(modelVehiculeUpdate);
    }

    @DeleteMapping("/deletemodelvehicule/{modelId}")
    public ResponseEntity<Void> deleteModelVehicule(@PathVariable Long modelId) {
        vehiculeService.deleteModelVehicule(modelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/Modelvehicule/{id}")
    public ResponseEntity<ModelVehicule> findModelvehiculeById(@PathVariable Long modelId) {
        ModelVehicule modelVehicule = vehiculeService.findById(modelId);
        return ResponseEntity.ok(modelVehicule);
    }

    @GetMapping("/allModelvehicules")
    public ResponseEntity<List<ModelVehicule>> findAllModelVehicule() {
        List<ModelVehicule> modelVehicules = vehiculeService.findAllModelVehicule();
        return ResponseEntity.ok(modelVehicules);
    }

    @PostMapping("/addCategorieVehicule")
    public ResponseEntity<CategorieVehicule> ajouterCategorieVehicule(@RequestBody CategorieVehicule categorieVehicule) {
        return ResponseEntity.ok().body(vehiculeService.ajouterCategorieVehicule(categorieVehicule));
    }

    @PutMapping("/updateCategorieVehicule/{id}")
    public ResponseEntity<CategorieVehicule> updateCategorieVehicule(@PathVariable(value = "id") Long categorieId, @RequestBody CategorieVehicule categorieVehicule) {
        CategorieVehicule updatedCategorieVehicule = vehiculeService.updateCategorieVehicule(categorieId, categorieVehicule);
        return ResponseEntity.ok(updatedCategorieVehicule);
    }

    @GetMapping("/categorieVehicule/{id}")
    public ResponseEntity<CategorieVehicule> findCategorieVehiculeById(@PathVariable Long id) {
        CategorieVehicule categorieVehicule = vehiculeService.findCategorieVehiculeById(id);
        if (categorieVehicule != null) {
            return ResponseEntity.ok(categorieVehicule);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteCategorieVehicule/{id}")
    public ResponseEntity<String> deleteCategorieVehicule(@PathVariable Long categorieId) {
        try {
            vehiculeService.deleteCategorieVehicule(categorieId);
            return ResponseEntity.status(HttpStatus.OK).body("La catégorie de véhicule a été supprimée avec succès");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La catégorie de véhicule avec l'ID " + categorieId + " n'existe pas");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible de supprimer la catégorie de véhicule avec l'ID " + categorieId + " car elle est liée à d'autres entités");
        }
    }

}
