package com.example.carrental.DTO;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "jwt_refresh_token")
public class JwtRefreshToken {
	
	@Id
    @Column(name = "token_id", nullable = false)
    String tokenId;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "create_date", nullable = false)
    LocalDateTime createDate;

    @Column(name = "expiration_date", nullable = false)
    LocalDateTime expirationDate;

}
