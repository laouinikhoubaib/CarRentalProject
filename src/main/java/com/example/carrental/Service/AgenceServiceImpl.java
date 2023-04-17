package com.example.carrental.Service;


import com.example.carrental.Enumerations.TypeAgence;
import com.example.carrental.Models.Agence;
import com.example.carrental.Models.Notification;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.AgenceRepository;
import com.example.carrental.Repository.NotificationRepository;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.ServiceInterfaces.AgenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.*;


@Slf4j
@Service
public class AgenceServiceImpl implements AgenceService {





    @Autowired
    UserRepository userrepository;
    @Autowired
    AgenceRepository agenceRepository;

    @Autowired
    NotificationRepository notificationRepository;
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
    public Agence addAgence(Agence agence) {

        Agence u = agenceRepository.save(agence);
        Notification notif = new Notification();
        notif.setCreatedAt(new Date());
        notif.setMessage("Nous sommes heureux d'avoir " + u.getNom()+ " notre nouvelle agnce !");
        notif.setRead(false);
        notificationRepository.save(notif);
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

    public Agence getAgenceByNom(String nom) {
        return agenceRepository.findByNom(nom);
    }

    public List<Agence> findByTypeAgence(TypeAgence typeAgence) {
        return agenceRepository.findByTypeagence(typeAgence);
    }

    public Map<String, Long> getCountByTypeAgence() {
        Map<String, Long> countByTypeAgence = new HashMap<>();
        countByTypeAgence.put("normale", agenceRepository.countByTypeagence(TypeAgence.normale));
        countByTypeAgence.put("franchise", agenceRepository.countByTypeagence(TypeAgence.franchise));
        return countByTypeAgence;
    }
}
