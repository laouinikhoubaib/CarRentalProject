package com.example.carrental.Repository;

import com.example.carrental.Models.UserDataLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDataLoadRepo extends JpaRepository<UserDataLoad,Long>{

}
