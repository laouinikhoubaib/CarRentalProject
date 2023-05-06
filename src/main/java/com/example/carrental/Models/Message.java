package com.example.carrental.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Message implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long messageId;


	@Column(name = "body")
	String body;
	
	@JsonIgnore
	@ManyToOne
	Chatroom chat;
}
