package com.example.carrental.Models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Media implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long mediaId;

	String name;
	
	String imagenUrl;
    
    String codeImage;

	public Media(String name, String imagenUrl, String imagencode) {

		this.name = name;
		this.imagenUrl = imagenUrl;
		this.codeImage = imagencode;
	}
    
}
