package com.example.carrental.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {
	
    @GetMapping
    public String homeController() {
    	return "Home Page !";
    }
    
    @GetMapping("/username")
    public Principal user(Principal principal) {
    	System.out.println("username:" + principal.getName());
    	return principal;
    }

}
