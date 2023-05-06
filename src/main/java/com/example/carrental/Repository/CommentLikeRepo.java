package com.example.carrental.Repository;

import com.example.carrental.Models.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentLikeRepo extends JpaRepository<CommentLike, Long>{

}
