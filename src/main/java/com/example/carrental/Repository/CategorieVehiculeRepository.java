package com.example.carrental.Repository;


import com.example.carrental.Models.CategorieVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieVehiculeRepository extends JpaRepository<CategorieVehicule, Long> {
}
