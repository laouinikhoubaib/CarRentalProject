package com.example.carrental.ServiceInterfaces;

import com.example.carrental.DTO.VehiculeDTO;
import com.example.carrental.Models.Vehicule;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

public interface VehiculeService {

    VehiculeDTO getById(final Integer vehiculeId);
    int addVehicule(Vehicule vehicule, @NotNull HttpServletRequest request);
    boolean updateVehicule(Vehicule vehicule);
    boolean deleteVehicule(int id);
    public List<VehiculeDTO> getAll();

    public List<VehiculeDTO> findAllOrderByPrixAsc();
    public List<VehiculeDTO> findTopNByOrderByVehiculedateDesc(Integer n);

    public List<VehiculeDTO> getVehiculesByRangePrix(double prix1, double prix2);

    double getChiffreAffaireByVehicule(int vehiculeId);

    public List<VehiculeDTO> findAllOrderByPrixDesc();
    public int getNumberVehiculeByUser(int idUser);

    public List<VehiculeDTO> getAvailableVehicules(LocalDate datedebut, LocalDate datefin);
}
