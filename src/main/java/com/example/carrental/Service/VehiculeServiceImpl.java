package com.example.carrental.Service;


import com.example.carrental.DTO.VehiculeDTO;
import com.example.carrental.Exceptions.NotFoundException;
import com.example.carrental.Models.*;
import com.example.carrental.Repository.NotificationRepository;
import com.example.carrental.Repository.ReservationRepository;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.ServiceInterfaces.VehiculeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@Slf4j
@Service
public class VehiculeServiceImpl implements VehiculeService {

    @Autowired
    VehiculeRepository vehiculeRepository;
    @Autowired
    UserRepository userRepository;
    @Inject
    private EntityManager entityManager;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserServiceImpl userService;

    @Autowired
    NotificationRepository notificationRepository;
    private VehiculeDTO mapToDTO(final Vehicule vehicule,
                                    final VehiculeDTO vehiculeDTO) {
        vehiculeDTO.setVehiculeId(vehicule.getVehiculeId());
        vehiculeDTO.setCharge_utile(vehicule.getChargeutile());
        vehiculeDTO.setCouleur(vehicule.getCouleur());
        vehiculeDTO.setMatricule(vehicule.getMatricule());
        vehiculeDTO.setDateajout(vehicule.getDateajout());
        vehiculeDTO.setPicture(vehicule.getPicture());
        vehiculeDTO.setJourslocation(vehicule.getJourslocation());
        vehiculeDTO.setNbr_places(vehicule.getNbrplaces());
        vehiculeDTO.setLargeur(vehicule.getLargeur());
        vehiculeDTO.setLongueur(vehicule.getLongueur());
        vehiculeDTO.setPuissance(vehicule.getPuissance());
        vehiculeDTO.setTypeUtilitaire(vehicule.getTypeUtilitaire());
        vehiculeDTO.setTypeVoiture(vehicule.getTypeVoiture());
        vehiculeDTO.setCategorie(vehicule.getCategorie());

        return vehiculeDTO;
    }

    @Override
    public VehiculeDTO getById(final Integer vehiculeId) {

        return vehiculeRepository.findById(vehiculeId)
                .map(vehiculeDTO -> mapToDTO(vehiculeDTO, new VehiculeDTO()))
                .orElseThrow(NotFoundException::new);
    }


    @Override
    public Vehicule addVehicule(Vehicule vehicule, String agenceName) {

        TypedQuery<Agence> query = entityManager.createQuery("SELECT a FROM Agence a WHERE a.nom = :nomAgence", Agence.class);
        query.setParameter("nomAgence", agenceName);
        List<Agence> agences = query.getResultList();
        Agence agence = agences.get(0);
        vehicule.setAgence(agence);
        Vehicule savedVehicule = vehiculeRepository.save(vehicule);
        Notification notif = new Notification();
        notif.setCreatedAt(new Date());
        notif.setMessage("Une nouvelle véhicule "  + savedVehicule.getVehiculeId()+   " est mise en service !");
        notif.setRead(false);
        notif.setVehicule(savedVehicule);
        notificationRepository.save(notif);
        return savedVehicule;
    }

    @Override
    public boolean updateVehicule(Vehicule vehicule) {

        if(vehiculeRepository.existsById(vehicule.getVehiculeId())){
            vehiculeRepository.save(vehicule).equals(vehicule);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteVehicule(int id) {
        if(vehiculeRepository.existsById(id)){
            vehiculeRepository.deleteById(id);
            return true;
        }else{
            return  false;
        }
    }

    @Override
    public List<VehiculeDTO> getAll() {
        final List<Vehicule> vehicules = vehiculeRepository.findAll(Sort.by("VehiculeId"));
        return vehicules.stream()
                .map((vehicule) -> mapToDTO(vehicule, new VehiculeDTO()))
                .collect(Collectors.toList());
    }

    public List<String> vehiculesdisponible(int vehiculeid){
        Vehicule vehicule=vehiculeRepository.findById(vehiculeid).get();
        String description=vehicule.getDescription();
        int vehiculeId=vehicule.getVehiculeId();
        String informationDisponibilite;
        Set<Reservation> reservationList=vehicule.getVehiculeReservationReservations();
        List<String> listDisponibilite=new ArrayList<>();
        List<String> dateLocations;
        for (Reservation reservation:reservationList){
            LocalDate datedebut= reservation.getDatedebut();
            LocalDate datefin= reservation.getDatefin();
            informationDisponibilite="Vehicule:"+vehiculeId+" " +" "+"est loué de :"+datedebut +" à "+datefin;
            listDisponibilite.add(informationDisponibilite);

        }
        return listDisponibilite;
    }
    @Override
    public List<VehiculeDTO> findAllOrderByPrixAsc() {
        final List<Vehicule> vehicules = vehiculeRepository.findAllOrderByPrixAsc();
        return vehicules.stream()
                .map((vehicule) -> mapToDTO(vehicule, new VehiculeDTO()))
                .collect(Collectors.toList());

    }
    @Override
    public List<VehiculeDTO> findAllOrderByPrixDesc(){
        final List<Vehicule> vehicules = vehiculeRepository.findAllVehiculesByPriceDESC();
        return vehicules.stream()
                .map((vehicule) -> mapToDTO(vehicule, new VehiculeDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public int getNumberVehiculeByUser(int idUser){
        List<Vehicule>rentalOfferList=vehiculeRepository.findVehiculesByUser(idUser);
        int        numberOfferByUser=0;
        for (Vehicule rentalOffer:rentalOfferList){
            numberOfferByUser+=1;
        }
        return numberOfferByUser;
    }

    @Override
    public List<VehiculeDTO> getAvailableVehicules(LocalDate datedebut,LocalDate datefin){
        List<Vehicule>vehiculeListAvailable=new ArrayList<>();
        List<Reservation>reservationList=reservationRepository.getReservationByDates(datedebut,datefin);
        List<Vehicule> list = vehiculeRepository.findAll().stream().filter(o ->
                !reservationList.stream().anyMatch(c -> o.getVehiculeReservationReservations().contains(c))).collect(Collectors.toList());
        return list.stream()
                .map((rentalOffer) -> mapToDTO(rentalOffer, new VehiculeDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculeDTO> findTopNByOrderByVehiculedateDesc(Integer n) {
        List<Vehicule> result = new ArrayList<>();
        List<Vehicule>vehicules= vehiculeRepository.findAll();
        vehicules.stream()
                .sorted(Comparator.comparing(Vehicule::getDateajout).reversed())
                .limit(n)
                .forEach(result::add);

        return result.stream()
                .map((vehicule) -> mapToDTO(vehicule, new VehiculeDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculeDTO> getVehiculesByRangePrix(double prix1, double prix2) {
        List<Vehicule> vehiculeList=new ArrayList<>();

        if (prix1<prix2){
            vehiculeList= vehiculeRepository.getVehiculesByRangePrix(prix1,prix2);
        }
        return vehiculeList.stream()
                .map((vehicule) -> mapToDTO(vehicule, new VehiculeDTO()))
                .collect(Collectors.toList());
    }


    @Override
    public double getChiffreAffaireByVehicule(int vehiculeId) {
        Vehicule vehicule= vehiculeRepository.findById(vehiculeId).get();
        Set<Reservation> reservationList=vehicule.getVehiculeReservationReservations();
        double revenueVehicule=0.0;
        for (Reservation reservation:reservationList){
            revenueVehicule+=  vehicule.getJourslocation()*reservation.getNbjour();
        }
        return revenueVehicule;
    }

}
