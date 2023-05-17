package com.example.carrental.ServiceInterfaces;

import com.example.carrental.DTO.VehiculeDTO;
import com.example.carrental.Models.Vehicule;

import java.util.List;

public interface VehiculeService {

    VehiculeDTO getById(final Integer vehiculeId);
    int addVehicule(Vehicule vehicule, int idUser);
    boolean updateVehicule(Vehicule vehicule);
    boolean deleteVehicule(int id);
    public List<VehiculeDTO> getAll();

    public List<VehiculeDTO> findAllOrderByPrixAsc();
    public List<VehiculeDTO> findTopNByOrderByVehiculedateDesc(Integer n);

    public List<VehiculeDTO> getVehiculesByRangePrix(double prix1, double prix2);

    double getChiffreAffaireByVehicule(int vehiculeId);
}
