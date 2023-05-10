package com.example.carrental.Repository;

import com.example.carrental.Models.CategoriePub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryAdvRepo extends JpaRepository<CategoriePub, Long>{

}
