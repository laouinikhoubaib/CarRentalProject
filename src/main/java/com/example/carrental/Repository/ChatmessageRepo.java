package com.example.carrental.Repository;

import com.example.carrental.Models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatmessageRepo extends JpaRepository<ChatMessage, Long>{

}
