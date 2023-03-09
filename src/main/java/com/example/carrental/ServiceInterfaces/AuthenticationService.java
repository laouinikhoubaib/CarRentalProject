package com.example.carrental.ServiceInterfaces;

import com.example.carrental.DTO.PasswordResetToken;
import com.example.carrental.Exceptions.EmailNotExist;
import com.example.carrental.Exceptions.ResetPasswordException;
import com.example.carrental.Exceptions.ResetPasswordTokenException;
import com.example.carrental.Models.User;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.jsonwebtoken.io.IOException;



public interface AuthenticationService
{
    User signInAndReturnJWT(User signInRequest);
	
    PasswordResetToken generatePasswordResetToken(String email) throws EmailNotExist, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException;
	
	void updatePassword(String token, String newPassword) throws ResetPasswordException, ResetPasswordTokenException;
}
