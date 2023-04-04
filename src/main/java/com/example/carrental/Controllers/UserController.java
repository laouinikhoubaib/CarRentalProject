package com.example.carrental.Controllers;


//import com.example.carrental.Models.Notification;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.ServiceInterfaces.UserService;
import com.example.carrental.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.carrental.Models.User;


import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/user")//pre-path
public class UserController
{
		
    @Autowired
    private UserService userService;
    
    @Autowired
    UserRepository userRepository;
    

    
    
    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
    	return userService.updateUser(user);
    }
    
    @GetMapping("/all")
    public List<User> findAllUsers()
    {
        return userService.findAllUsers();
    }
    

    
//    @PostMapping("/notification/save/{username}")
//    public Notification addNotification(@RequestBody Notification notification,@PathVariable(name="username") String username) {
//    	return userService.addNotification(notification, username);
//    }
//
//    @DeleteMapping("/notification/delete/{notificationId}")
//    public void deleteNotification(@PathVariable(name="notificationId") Long notificationId) {
//    	userService.deleteNotification(notificationId);
//    }
//
//    @GetMapping("/notifications/all")
//    public List<Notification> findAllNotifications() {
//    	return userService.findAllNotifications();
//    }
//
//
//    @PutMapping("/notification/read")
//    public void markNotifAsRead(@RequestBody Long idNotif) {
//    	userService.markNotifAsRead(idNotif);
//    }
//
//    @PutMapping("/notification/unread")
//    public void markNotifAsUnRead(@RequestBody Long idNotif) {
//    	userService.markNotifAsUnRead(idNotif);
//    }

    @GetMapping("/picture")
    public String getUserProfilPic( @AuthenticationPrincipal UserPrincipal user) {
        return userService.getUserProfilPic(user.getId());
    }
    
    @GetMapping("/picture2")
    public String getUserProfilPic2(@RequestParam Long userId) {
    	return userService.getUserProfilPic(userId);
    }
    
    @GetMapping("/{userId}")
    public User getUser(@PathVariable(value="userId") Long userId) {
    	return userService.getUser(userId);
    }

    @GetMapping("/same-agence")
    public ResponseEntity<List<User>> getUsersBySameAgence(@RequestParam("userId") Long userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<User> users = userService.findUsersByAgence(user);
        return ResponseEntity.ok(users);
    }

    
}
