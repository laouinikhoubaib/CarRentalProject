package com.example.carrental.Repository;

import com.example.carrental.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>{

}
