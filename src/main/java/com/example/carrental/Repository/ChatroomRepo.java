package com.example.carrental.Repository;

import com.example.carrental.Models.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepo extends JpaRepository<Chatroom, Long>{

}
