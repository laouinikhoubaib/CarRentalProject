package com.example.carrental.Models;

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
public class BadWord implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long IdWord;

	@Column(name = "word")
	String word;

}
