package com.example.carrental.websocketproject;

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
	
	String body;
	
	@JsonIgnore
	@ManyToOne
	Chatroom chat;
}
