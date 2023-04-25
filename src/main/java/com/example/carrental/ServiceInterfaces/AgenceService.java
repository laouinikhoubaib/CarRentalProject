package com.example.carrental.ServiceInterfaces;

import com.example.carrental.Enumerations.TypeAgence;
import com.example.carrental.Models.Agence;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AgenceService {

    public Agence addAgence(Agence agence);

    List<Agence> getAgences();

    Agence getAgenceById(Long agenceId);

    public Agence updateAgence(Agence agence);

    void deleteAgence(Long agenceId);

    List<Agence> findAllAgences();

    public Agence getAgenceByNom(String nom);


    public List<Agence> findByTypeAgence(TypeAgence typeAgence);

    public Map<String, Long> getCountByTypeAgence();

    public String agenceDetails(Long agenceId) throws IOException;

    void lockAgence(String nom);

}
