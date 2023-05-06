package com.example.carrental.Repository;

import com.example.carrental.Models.BadWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BadWordRepo extends JpaRepository<BadWord, Long>{

}
