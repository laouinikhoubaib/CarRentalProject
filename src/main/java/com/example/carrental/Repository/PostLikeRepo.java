package com.example.carrental.Repository;

import com.example.carrental.Models.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Set;
@Repository
public interface PostLikeRepo extends JpaRepository<PostLike, Long>{

	
	@Query(value =" SELECT * from users u  INNER JOIN post_like p ON p.user_user_id = u.user_id ORDER BY count(*)",nativeQuery=true)
			public Set<Object> USer_order_by_Like ();
}
