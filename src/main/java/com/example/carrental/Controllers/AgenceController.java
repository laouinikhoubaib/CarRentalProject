package com.example.carrental.Controllers;


import com.example.carrental.Exceptions.AgenceExist;
import com.example.carrental.Exceptions.AgenceNotExist;
import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.AgenceRepository;
import com.example.carrental.ServiceInterfaces.AgenceService;
import com.example.carrental.ServiceInterfaces.UserService;
import com.example.carrental.security.UserPrincipal;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
    @ResponseBody
    public Agence addAgence(@RequestBody Agence agence){
        return agenceService.addAgence(agence);

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



}
