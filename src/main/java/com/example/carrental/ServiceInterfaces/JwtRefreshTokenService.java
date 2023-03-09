package com.example.carrental.ServiceInterfaces;



import com.example.carrental.DTO.JwtRefreshToken;
import com.example.carrental.Models.User;


public interface JwtRefreshTokenService
{
    JwtRefreshToken createRefreshToken(Long userId);

    User generateAccessTokenFromRefreshToken(String refreshTokenId);
}
