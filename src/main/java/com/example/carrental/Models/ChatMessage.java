package com.example.carrental.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChatMessage {




	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long messageId;

	@Column(name = "username")
	private String username;

	@Column(name = "text")
    private String text;

	@Column(name = "avatar")
    private String avatar;
    
	Long idchat;
    
	Long sender;

	@JsonIgnore
	@ManyToOne
	Chatroom chat;



	
}
