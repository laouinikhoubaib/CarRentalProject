package com.example.carrental.Repository;


import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long>{



  //  Agence findByNom(String nom);


}
