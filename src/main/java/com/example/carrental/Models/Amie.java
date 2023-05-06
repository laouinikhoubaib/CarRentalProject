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
public class Amie implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long friendId;
	
	@Temporal(TemporalType.DATE)
	Date createdAt;
	
	@OneToOne(cascade = CascadeType.ALL)
	User sender;
	
	@OneToOne(cascade = CascadeType.ALL)
	User receiver;

}
