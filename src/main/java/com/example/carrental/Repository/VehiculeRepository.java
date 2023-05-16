package com.example.carrental.Repository;



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
    List<Vehicule> findRentalOfferByRangePrice(@Param("prix1") double prix1, @Param("prix2")  double prix2);
}

