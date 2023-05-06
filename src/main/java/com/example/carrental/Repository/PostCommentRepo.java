package com.example.carrental.Repository;

import com.example.carrental.Models.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostCommentRepo extends JpaRepository<PostComment, Long>{

}
