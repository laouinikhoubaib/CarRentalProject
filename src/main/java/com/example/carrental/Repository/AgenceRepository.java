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



//    @Query(nativeQuery = true, value = "SELECT u.* FROM users u WHERE u.agence_agence_id = :agence_agence_id")
//    public List<User> findUsersByAgence(@Param("agence_agence_id") Long agence_agence_id);


}
