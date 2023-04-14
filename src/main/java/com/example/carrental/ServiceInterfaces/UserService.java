package com.example.carrental.ServiceInterfaces;


import com.example.carrental.Enumerations.Role;
import com.example.carrental.Exceptions.EmailExist;
import com.example.carrental.Exceptions.UsernameExist;
import com.example.carrental.Exceptions.UsernameNotExist;
import com.example.carrental.Models.Agence;
//import com.example.carrental.Models.Notification;
import com.example.carrental.Models.Notification;
import com.example.carrental.Models.User;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.jsonwebtoken.io.IOException;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;



public interface UserService
{
    User saveUser(User user, String nomAgence) throws UsernameNotExist, UsernameExist, EmailExist, MessagingException, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException;

    Optional<User> findByUsername(String username);

    User getUser(Long userId);
    
	Optional<User> findByEmail(String email);

    void changeRole(Role newRole, String username);
    
    void makeAdmin(String username);

    List<User> findAllUsers();


    void unlockUser(String username);

    void lockUser(String username);

	User updateUser(User user);
	
	String getUserProfilPic(Long userId);

	
	public List<User> allAdmins();


    List<Notification> findNotificationsByUser(Long userId);

//    Notification addNotification(Notification notification, String username);

    void deleteNotification(Long notificationId);

    List<Notification> findAllNotifications();


    void markNotifAsRead(Long idNotif);

    void markNotifAsUnRead(Long idNotif);


    public User findUserById(Long userId);
    public List<User> findUsersByAgence(User user);

    public List<User> findAdminByNomAgence(String nomAgence);

    public List<User> getUserByAgence(Long agenceId);

    public String getAgencyNameByUserId(Long userId);
}
