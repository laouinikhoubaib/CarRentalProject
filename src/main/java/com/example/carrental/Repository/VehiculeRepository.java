package com.example.carrental.Repository;



import com.example.carrental.DTO.VehiculeDTO;
import com.example.carrental.Enumerations.Categorie;
import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import com.example.carrental.Models.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer> {



    @Query("SELECT p FROM Vehicule p ORDER BY p.jourslocation ASC")
    List<Vehicule> findAllOrderByPrixAsc();

    @Query("SELECT p FROM Vehicule p WHERE p.jourslocation< :prix2 and p.jourslocation> :prix1")
    List<Vehicule> getVehiculesByRangePrix(@Param("prix1") double prix1, @Param("prix2")  double prix2);

    @Query("SELECT p FROM Vehicule p ORDER BY p.jourslocation DESC")
    List<Vehicule> findAllVehiculesByPriceDESC();

    @Query("SELECT p FROM Vehicule p WHERE  p.user.userId=:idUser")
    List<Vehicule> findVehiculesByUser(int idUser);

    List<Vehicule> findByCategorie(Categorie categorie);

    @Query("SELECT a FROM Vehicule a WHERE a.matricule = :matricule")
    Vehicule findByMatricule(@Param("matricule") String matricule);

    List<Vehicule> findByAgence(Agence agence);

    List<Vehicule> findByAgenceAndCategorie(Agence agence, Categorie categorie);

}

