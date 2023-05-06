package com.example.carrental.Models;

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
public class PostDislike implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long postDislikeId;

	@Temporal(TemporalType.DATE)
	Date dislikedAt;

	@ManyToOne
	User user;

	@ManyToOne
	Post post;

}
