package com.example.carrental.Controllers;


import com.example.carrental.Enumerations.Role;
import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import com.example.carrental.ServiceInterfaces.AgenceService;
import com.example.carrental.ServiceInterfaces.UserService;
import com.example.carrental.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/admin")//pre-path

public class AdminController
{


    @Autowired
    private UserService userService;

    @Autowired
    private AgenceService agenceService;



    @GetMapping("all")//api/admin/all
    public ResponseEntity<?> findAllUsers()
    {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PutMapping("/lock")
    public ResponseEntity<?> lockUser(@RequestBody String username) {
        userService.lockUser(username);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/unlock")
    public ResponseEntity<?> unlockUser(@RequestBody String username) {
        userService.unlockUser(username);
        return ResponseEntity.ok(true);
    }

    @PutMapping("change/{role}")//api/user/change/{role}
    public ResponseEntity<?> changeRole(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Role role)
    {
        userService.changeRole(role, userPrincipal.getUsername());
        return ResponseEntity.ok(true);
    }

    @PutMapping("makeAdmin/{username}")
    public ResponseEntity<?> makeAdmin(@PathVariable(value="username") String username) {
        userService.makeAdmin(username);
        return ResponseEntity.ok(true);
    }
    @GetMapping("/admins")
    public List<User> allAdmins(){
    	return userService.allAdmins();
    }

    @GetMapping("/{agenceId}/users")
    public ResponseEntity<List<User>> getUsersByAgence(@PathVariable Long agenceId) {
        Agence agence = new Agence(agenceId);
        List<User> users = userService.getUsersByAgence(agence);
        return ResponseEntity.ok(users);
    }


}
