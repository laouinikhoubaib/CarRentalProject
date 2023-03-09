package com.example.carrental.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
@Table(name = "password_reset_token")
public class PasswordResetToken implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
	Long passwordResetTokenId;
	
	String token;
	
	Long userId;;
	
	LocalDateTime createDate;
	
	LocalDateTime exprirationDate;

}
