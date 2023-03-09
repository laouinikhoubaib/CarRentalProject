package com.example.carrental.Repository;

import com.example.carrental.Models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MediaRepo extends JpaRepository<Media, Long> {
	
}
