package com.example.carrental.Service;


import com.example.carrental.Models.Agence;
import com.example.carrental.Repository.AgenceRepository;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.ServiceInterfaces.AgenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@Service
public class AgenceServiceImpl implements AgenceService {






    @Autowired
    AgenceRepository agenceRepository;

    public AgenceServiceImpl(AgenceRepository agenceRepository) {
        this.agenceRepository = agenceRepository;

    }

    @Override
    public List<Agence> getAgences(){

        List<Agence> agences = new ArrayList<>();
        agenceRepository.findAll().forEach(agences::add);
        return agences;
    }


    @Override
    public Agence saveAgence(Agence agence) {
        return agenceRepository.save(agence);
    }

    @Override
    public Agence getAgenceById(Long agenceId) {

        return agenceRepository.findById(agenceId).get();
    }


    @Override
    public void deleteAgence(Long agenceId) {

        agenceRepository.deleteById(agenceId);
    }

    @Override
    public List<Agence> findAllAgences() {
        return agenceRepository.findAll();
    }


    @Override
    public Agence updateAgence(Agence agence) {

        return agenceRepository.save(agence);
    }


}
