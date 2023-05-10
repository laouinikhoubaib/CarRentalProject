package com.example.carrental.websocketproject;

import com.example.carrental.Models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Chatroom implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long chatroomId;
	
	@JsonIgnore
	@ManyToOne
	User sender;
	@JsonIgnore
	@ManyToOne 
	User reciver;
	
	
	String color = "#EC407A";

	
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "chat")
	List<ChatMessage> messages;
}
