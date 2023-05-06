package com.example.carrental.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class PostComment implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long postCommentId;
	
	String commentBody;

	@Temporal(TemporalType.DATE)
	Date commentedAt;
	
	
	@ManyToOne
	User user;
	
	@JsonIgnore
	@ManyToOne
	Post post;

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "postCo")
	Set<PostComment> postComments;
	@JsonIgnore
	@ManyToOne
	PostComment postCo;
	

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "postComment")
	Set<CommentLike> commentLikes;

}
