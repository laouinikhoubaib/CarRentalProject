package com.example.carrental.Models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class CategoriePub {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long idCategoryAdv;

	@Column(name = "word")
	String nomCategorie;
}
