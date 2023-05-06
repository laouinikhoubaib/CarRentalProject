package com.example.carrental.Repository;

import com.example.carrental.Models.PostDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostDislikeRepo extends JpaRepository<PostDislike, Long>{

}
