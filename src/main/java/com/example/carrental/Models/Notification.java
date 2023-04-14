package com.example.carrental.Models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Notification implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long notificationId;

	String message;

	@Temporal(TemporalType.DATE)
	Date createdAt;

	boolean isRead;

	@ManyToOne
	User user;

	@ManyToOne
	Vehicule vehicule;

	@OneToOne
	Complaint complaint;
}
