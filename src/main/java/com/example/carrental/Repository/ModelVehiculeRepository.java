package com.example.carrental.Repository;

import com.example.carrental.Models.ModelVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ModelVehiculeRepository extends JpaRepository<ModelVehicule, Long> {
}
