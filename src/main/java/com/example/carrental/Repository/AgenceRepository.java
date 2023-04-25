package com.example.carrental.Repository;


import com.example.carrental.Enumerations.TypeAgence;
import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long>{


    @Query("SELECT a FROM Agence a WHERE a.nom = :nom")
    Agence findByNom(@Param("nom") String nom);

    @Query("SELECT COUNT(a) FROM Agence a WHERE a.typeagence = :typeagence")
    Long countByTypeagence(@Param("typeagence") TypeAgence typeagence);

    List<Agence> findByTypeagence(TypeAgence typeAgence);


}
