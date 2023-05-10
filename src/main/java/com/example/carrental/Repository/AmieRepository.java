package com.example.carrental.Repository;

import com.example.carrental.Models.Amie;
import com.example.carrental.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AmieRepository extends JpaRepository<Amie, Long> {
	
    boolean existsBySenderAndReceiver(User sender, User receiver);
    
    boolean existsByReceiverAndSender(User sender,User receiver);

    List<Amie> findBySender(User user);
    List<Amie> findByReceiver(User user);

}
