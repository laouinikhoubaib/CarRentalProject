package com.example.carrental.Controllers;


import com.example.carrental.DTO.VehiculeDTO;
import com.example.carrental.Models.User;
import com.example.carrental.Models.Vehicule;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.Service.VehiculeServiceImpl;
import com.example.carrental.ServiceInterfaces.VehiculeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/vehicule")
@CrossOrigin(origins = "http://localhost:4200")
public class VehiculeController {

    @Autowired
    VehiculeService vehiculeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VehiculeServiceImpl vehiculeServiceimpl;

    @Autowired
    VehiculeRepository vehiculeRepository;


    @PostMapping("/addVehicule")
    public ResponseEntity<String> addVehicule(@RequestBody Vehicule vehicule,  @NotNull HttpServletRequest request){
        int newvehiculeId = vehiculeService.addVehicule(vehicule, request);
        if (newvehiculeId != 0) {
            String message = "Véhicule  ajoutée avec succès avec l'id " + newvehiculeId;
            return ResponseEntity.ok().body(message);
        } else {
            String message = "Erreur lors de l'ajout de véhicule";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping("/updateVehicule")
    public ResponseEntity<String> updateVehicule(@RequestBody Vehicule vehicule){
        boolean isUpdated = vehiculeService.updateVehicule(vehicule);
        if (isUpdated) {
            String message = "Véhicule de location mise à jour avec succès";
            return ResponseEntity.ok().body(message);
        } else {
            String message = "Erreur lors de la mise à jour de véhicule";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @DeleteMapping("/deleteVehicule/{vehicule}")
    public ResponseEntity<String> deleteVehicule(@PathVariable("vehicule") int vehicule) {
        boolean isDeleted = vehiculeService.deleteVehicule(vehicule);
        if (isDeleted) {
            String message = "Véhicule de location supprimée avec succès";
            return ResponseEntity.ok().body(message);
        } else {
            String message = "Erreur lors de la suppression de Véhicule";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }
    @GetMapping("/GetAllVehicules")
    public List<VehiculeDTO> getAllVehicules(){
        List<VehiculeDTO> listvehicules = vehiculeService.getAll();
        return listvehicules;
    }

    @GetMapping("/{vehicule}")
    public ResponseEntity<VehiculeDTO> getVehicule(@PathVariable("vehicule") Integer vehicule){
        VehiculeDTO vehiculeDTO = vehiculeService.getById(vehicule);
        if (vehiculeDTO != null) {
            return ResponseEntity.ok(vehiculeDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/Disponibilite/{vehiculeid}")
    public ResponseEntity<List<String>> getVehicule(@PathVariable("vehiculeid") int vehiculeid){
        List<String> disponibilite = vehiculeServiceimpl.vehiculesdisponible(vehiculeid);
        if (disponibilite.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            String message = "Disponibilité de la vehicule avec l'id " + vehiculeid;
            return ResponseEntity.ok().body(disponibilite);
        }
    }

    @GetMapping("/triDesc")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesOrderByPriceDesc() {
        List<VehiculeDTO> vehicules = vehiculeService.findAllOrderByPrixDesc();
        if (vehicules.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            String message = "Liste des offres de location triées par prix en ordre ascendant récupérée avec succès";
            return ResponseEntity.ok().header("message", message).body(vehicules);
        }
    }

    @GetMapping("/getNumberVehiculesByUser/{userId}")
    public ResponseEntity<?> getNumberVehiculesByUser(@PathVariable("userId") int idUser) {
        int numberVehiculesByUser = vehiculeService.getNumberVehiculeByUser(idUser);

        String message = "Le nombre d'offre pour cette utilisateur est :" + numberVehiculesByUser;
        return ResponseEntity.status(HttpStatus.OK.value()).body(message);

    }
    @GetMapping("/GetAvailableVehicules/{datedebut}/{datefin}")
    public List<VehiculeDTO> GetAvailableVehicules(@PathVariable("datedebut") String datedebut,@PathVariable("datefin")String datefin){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<VehiculeDTO> listContrats = vehiculeService.getAvailableVehicules(LocalDate.parse(datedebut,formatter),LocalDate.parse(datefin,formatter));
        return listContrats;
    }
    @GetMapping("/getNTopVehicules/{vehicule}")
    public ResponseEntity<?> getVehiculesByRangePrice(@PathVariable("vehicule") Integer vehicule){
        List<VehiculeDTO> vehiculeDTOList = vehiculeService.findTopNByOrderByVehiculedateDesc(vehicule);
        if (!vehiculeDTOList.isEmpty()) {
            return ResponseEntity.ok(vehiculeDTOList);
        } else {
            String errorMessage = "Aucune offre de location trouvée pour le nombre demandé de " + vehicule;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @GetMapping("/getRentalOffersByRangePrice/{prix1}/{prix2}")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesByRangePrice(@PathVariable("prix1")double prix1,@PathVariable("prix2")double prix2){
        List<VehiculeDTO> vehicules = vehiculeService.getVehiculesByRangePrix(prix1,prix2);
        if (vehicules.isEmpty()) {
            String message = "Il n'existe pas des véhicules de location entre les prix " + prix1 + " et " + prix2;

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            String message = "Liste des véhicules de location entre les prix " + prix1 + " et " + prix2;
            return ResponseEntity.ok().body(vehicules);
        }
    }

    @GetMapping("/tri")
    public ResponseEntity<List<VehiculeDTO>> getRentalOffersOrderByPriceAsc() {
        List<VehiculeDTO> vehicules = vehiculeService.findAllOrderByPrixAsc();
        if (vehicules.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            String message = "Liste des véhicules de location triées par prix en ordre ascendant récupérée avec succès";
            return ResponseEntity.ok().header("message", message).body(vehicules);
        }
    }

    @GetMapping("/revenue/{vehiculeId}")
    public double calculateRevenueForUser(@PathVariable int vehiculeId) {
        Double revenue = vehiculeService.getChiffreAffaireByVehicule(vehiculeId);
        if (revenue == null) {
            return 0;
        } else {
            return revenue;
        }
    }


}
