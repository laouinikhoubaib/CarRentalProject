package com.example.carrental.Repository;


import com.example.carrental.Enumerations.Role;
import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

;

@Repository
public interface UserRepository<C extends User, L extends Number> extends JpaRepository<User, Long> {
	
    Optional<User> findByUsername(String username);


    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User set role = :role where username = :username")
    void updateUserRole(@Param("username") String username, @Param("role") Role role);
    
    @Modifying
    @Query("update User set role = 'ADMIN' where username = :username")
    void makeAdmin(@Param("username") String username);


    List<User> findByAgence(Agence agence);
   
    
}
