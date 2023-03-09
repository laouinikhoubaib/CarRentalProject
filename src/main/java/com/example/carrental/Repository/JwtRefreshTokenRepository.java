package com.example.carrental.Repository;

import com.example.carrental.DTO.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;



public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, String>
{
}
