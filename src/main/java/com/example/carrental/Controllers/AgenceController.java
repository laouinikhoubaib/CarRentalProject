package com.example.carrental.Controllers;


import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.AgenceRepository;
import com.example.carrental.ServiceInterfaces.AgenceService;
import com.example.carrental.ServiceInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/agence")
@CrossOrigin(origins = "http://localhost:4200")
public class AgenceController {


    AgenceService agenceService;
    @Autowired
    UserService userService;
    @Autowired
    AgenceRepository agenceRepository;



    public AgenceController(AgenceService agenceService) {
        this.agenceService = agenceService;
    }

    @PostMapping("/addAgence")
    public ResponseEntity<Agence> createAgence(@RequestBody Agence agences) {
        try {
            Agence agence  = agenceRepository.save(new Agence(agences.getNumeroAgence(),agences.getNomAgence(),agences.getTypeagence(),agences.getUsers()));
            return new ResponseEntity<>(agence, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({"/deleteAgence/{agenceId}"})
    public ResponseEntity<Agence> deleteAgence(@PathVariable("agenceId") Long agenceId) {
        agenceService.deleteAgence(agenceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteAllAgences")
    public ResponseEntity<HttpStatus> deleteAllAgences() {
        try {
            agenceRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping({"/updateAgence"})
    public Agence updateAgence(@RequestBody Agence agence) {
        return agenceService.updateAgence(agence);

    }

    @GetMapping({"/getAgence/{agenceId}"})
    public ResponseEntity<Agence> getAgence(@PathVariable Long agenceId) {
        return new ResponseEntity<>(agenceService.getAgenceById(agenceId), HttpStatus.OK);
    }


    @GetMapping("/all")
    public List<Agence> findAllAgences()
    {
        return agenceService.findAllAgences();
    }

//    @GetMapping("/getBynom/nomAgence")
//    public ResponseEntity<List<Agence>> getAgence(@RequestParam(required = false) String nomAgence) {
//        try {
//            List<Agence> agences = new ArrayList<Agence>();
//            if (nomAgence == null)
//                agenceRepository.findAll().forEach(agences::add);
//            else
//                agenceRepository.getAgence(nomAgence).forEach(agences::add);
//            if (agences.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(agences, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
    @PostMapping("/affectatUserToagence/{userId}/{agenceId}")
    @ResponseBody
    public void affectatUserToAgence(@PathVariable("userId") Long userId,@PathVariable("agenceId")Long agenceId) throws IOException {

        userService.affectatUserToAgence(agenceId,userId);
   }

}
