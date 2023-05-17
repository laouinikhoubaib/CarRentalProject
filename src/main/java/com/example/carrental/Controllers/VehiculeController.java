package com.example.carrental.Controllers;


import com.example.carrental.DTO.VehiculeDTO;
import com.example.carrental.Models.Vehicule;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.Service.VehiculeServiceImpl;
import com.example.carrental.ServiceInterfaces.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/addVehicule/{idUser}")
    public ResponseEntity<String> addVehicule(@RequestBody Vehicule vehicule, @PathVariable("idUser") int idUser){
        int newvehiculeId = vehiculeService.addVehicule(vehicule, idUser);
        if (newvehiculeId != 0) {
            String message = "Véhicule  ajoutée avec succès avec l'id " + newvehiculeId;
            return ResponseEntity.ok().body(message);
        } else {
            String message = "Erreur lors de l'ajout de véhicule";
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
}
