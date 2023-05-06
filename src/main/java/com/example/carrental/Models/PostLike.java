package com.example.carrental.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class PostLike implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long postLikeId;

	@Temporal(TemporalType.DATE)
	Date likedAt;
	
	Boolean isLiked ;
	
	
	@ManyToOne
	User user;
	
	@JsonIgnore
	@ManyToOne
	Post post;
	

}
