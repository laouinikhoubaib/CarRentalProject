package com.example.carrental.Service;


import com.example.carrental.DTO.VehiculeDTO;
import com.example.carrental.Exceptions.NotFoundException;
import com.example.carrental.Models.Reservation;
import com.example.carrental.Models.User;
import com.example.carrental.Models.Vehicule;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.ServiceInterfaces.VehiculeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class VehiculeServiceImpl implements VehiculeService {

    @Autowired
    VehiculeRepository vehiculeRepository;
    @Autowired
    UserRepository userRepository;

    private VehiculeDTO mapToDTO(final Vehicule vehicule,
                                    final VehiculeDTO vehiculeDTO) {
        vehiculeDTO.setVehiculeId(vehicule.getVehiculeId());
        vehiculeDTO.setCharge_utile(vehicule.getCharge_utile());
        vehiculeDTO.setCouleur(vehicule.getCouleur());
        vehiculeDTO.setMatricule(vehicule.getMatricule());
        vehiculeDTO.setDateajout(vehicule.getDateajout());
        vehiculeDTO.setPicture(vehicule.getPicture());
        vehiculeDTO.setJourslocation(vehicule.getJourslocation());
        vehiculeDTO.setNbr_places(vehicule.getNbr_places());
        vehiculeDTO.setLargeur(vehicule.getLargeur());
        vehiculeDTO.setLongueur(vehicule.getLongueur());
        vehiculeDTO.setPuissance(vehicule.getPuissance());

        return vehiculeDTO;
    }

    public static String saveImage(MultipartFile image, Vehicule vehicule) throws IOException {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        Path path = Paths.get("uploads");
        Files.createDirectories(path);
        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            vehicule.setPicture(filePath.toString());
            return filePath.toString();
        } catch (IOException e) {
            throw new IOException("Impossible d'enregistrer l'image' " +" "+ fileName, e);
        }
    }
    @Override
    public VehiculeDTO getById(final Integer vehiculeId) {

        return vehiculeRepository.findById(vehiculeId)
                .map(vehiculeDTO -> mapToDTO(vehiculeDTO, new VehiculeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public int addVehicule(Vehicule vehicule, int idUser){
        User user=userRepository.findById((long) idUser).get();
        vehicule.setUser(user);
        return vehiculeRepository.save(vehicule).getVehiculeId();
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
        final List<Vehicule> rentalOffers = vehiculeRepository.findAllOrderByPrixAsc();
        return rentalOffers.stream()
                .map((vehicule) -> mapToDTO(vehicule, new VehiculeDTO()))
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
                .collect(Collectors.toList());    }
}
