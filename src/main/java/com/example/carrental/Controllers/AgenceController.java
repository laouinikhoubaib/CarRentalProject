package com.example.carrental.Controllers;


import com.example.carrental.Enumerations.TypeAgence;
import com.example.carrental.Models.Agence;
import com.example.carrental.Repository.AgenceRepository;
import com.example.carrental.ServiceInterfaces.AgenceService;
import com.example.carrental.ServiceInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    @ResponseBody
    public Agence addAgence(@RequestBody Agence agence){
        return agenceService.addAgence(agence);

    }

    @DeleteMapping({"/deleteAgence/{agenceId}"})
    @ResponseBody
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


    @GetMapping("/{nom}")
    public ResponseEntity<Agence> getAgenceByNom(@PathVariable String nom) {
        Agence agence = agenceService.getAgenceByNom(nom);
        if (agence == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(agence);
    }

    @GetMapping("/typeagence/{typeAgence}")
    public ResponseEntity<List<Agence>> findByTypeAgence(@PathVariable("typeAgence") TypeAgence typeAgence) {
        List<Agence> agences = agenceService.findByTypeAgence(typeAgence);
        return ResponseEntity.ok(agences);
    }
    @GetMapping("/countByType")
    public Map<String, Long> getCountByTypeAgence() {
        return agenceService.getCountByTypeAgence();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<String> agenceDetails(@PathVariable Long id) throws IOException {
        String result = agenceService.agenceDetails(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/block")
    public Agence blockAgence(@RequestBody String nom) {
        return agenceService.blockAgence(nom);
    }

    @PutMapping("/deblock")
    public Agence deblockAgence(@RequestBody String nom) {
        return agenceService.deblockAgence(nom);
    }

}
