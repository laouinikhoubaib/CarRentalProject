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
public class Post implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long postId;

	@Column(name = "postTitle")
	String postTitle;


	@Column(name = "body")
	String body;

	@Temporal(TemporalType.DATE)
	Date createdAt;

	@Column(name = "nb_Signal")
	int nb_Signal;

	@Column(name = "nb_etoil")
	int nb_etoil;
	

	@ManyToOne 
	User user;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<PostLike> postLikes;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<PostDislike> postDislikes;
	

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<PostComment> postComments;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	Set<User> reportedby;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	Set<Media> medias;
}
