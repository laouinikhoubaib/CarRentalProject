package com.example.carrental.Service;


import com.example.carrental.Enumerations.Role;
import com.example.carrental.Exceptions.EmailExist;
import com.example.carrental.Exceptions.PasswordValidException;
import com.example.carrental.Exceptions.UsernameExist;
import com.example.carrental.Exceptions.UsernameNotExist;
import com.example.carrental.Models.Agence;
//import com.example.carrental.Models.Notification;
import com.example.carrental.Models.Notification;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.*;
import com.example.carrental.ServiceInterfaces.UserService;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.mail.MessagingException;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;


@Slf4j
@Service
public class UserServiceImpl implements UserService
{

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    
    @Autowired
    ServiceAllEmail emailService;


    @Autowired
	MediaRepo mediaRepository;

	@Autowired
	AgenceRepository agenceRepository;


	@Inject
	private EntityManager entityManager;

	@Autowired
	NotificationRepository notificationRepository;


	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}
    @Override
	public User saveUser(User user, String agenceName) throws UsernameNotExist, UsernameExist, EmailExist, MessagingException, io.jsonwebtoken.io.IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException, MessagingException {
		isvalidUsernameAndEmail(EMPTY, user.getUsername(), user.getEmail());
		isValid(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setLocked(false);

		TypedQuery<Agence> query = entityManager.createQuery("SELECT a FROM Agence a WHERE a.nom = :nomAgence", Agence.class);
		query.setParameter("nomAgence", agenceName);
		List<Agence> agences = query.getResultList();
		Agence agence = agences.get(0);
		user.setAgence(agence);
		User savedUser = userRepository.save(user);
		Notification notif = new Notification();
		notif.setCreatedAt(new Date());
		notif.setMessage("Nous sommes heureux d'avoir"  + savedUser.getUsername()+   " dans notre communauté !");
		notif.setRead(false);
		notif.setUser(savedUser);
		notificationRepository.save(notif);
		emailService.sendWelcomeMail(savedUser.getUsername(), savedUser.getEmail());
		return savedUser;
	}

	@Override
    public User updateUser(User user) {

		return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username)
    { 

		return userRepository.findByUsername(username);
    }


    @Override
    public Optional<User> findByEmail(String email)
    {

		return userRepository.findByEmail(email);
    }


    @Override
    public List<User> findAllUsers()
    {

		return userRepository.findAll();
    }

	@Override
	public User getUser(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		return user;
	}

	@Override
	public void unlockUser(String username) {
		User u = userRepository.findByUsername(username).get();
		u.setLoginAttempts(0);
		u.setLocked(false);

		Notification notif = new Notification();
		notif.setCreatedAt(new Date());
		notif.setMessage("Nous sommes heureux d'avoir "  + u.getUsername()+   " encore parmi nous !");
		notif.setRead(false);
		notif.setUser(u);
		notificationRepository.save(notif);
		userRepository.save(u);
	}


	@Override
	public String getUserProfilPic(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		return user.getProfilPicture().getImagenUrl();
	}

	

	 private User isvalidUsernameAndEmail(String currentUsername, String newUsername, String newEmail) 
			 throws UsernameNotExist, UsernameExist, EmailExist {
	        User userByNewUsername = findByUsername(newUsername).orElse(null);
	        User userByNewEmail = findByEmail(newEmail).orElse(null);
	        if(StringUtils.isNotBlank(currentUsername)) {
	            User currentUser = findByUsername(currentUsername).orElse(null);
	            if(currentUser == null) {
	                throw new UsernameNotExist("Aucun utilisateur trouvé par nom d’utilisateur: " + currentUsername);
	            }
	            if(userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
	                throw new UsernameExist("Username exist dèja");
	            }
	            if(userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
	                throw new EmailExist("Email exist dèja");
	            }
	            return currentUser;
	        } else {
	            if(userByNewUsername != null) {
	                throw new UsernameExist("Username exist dèja");
	            }
	            if(userByNewEmail != null) {
	                throw new EmailExist("Email exist dèja");
	            }
	            return null;
	        }
	    }
	 
	 @SneakyThrows
	 public boolean isValid(String password) {
		 String messageTemplate = null;
		 Properties props = new Properties();
		 InputStream inputStream = getClass().getClassLoader().getResourceAsStream("passay.properties");
		 try {
			 props.load(inputStream);
		 } catch (IOException e) {
			 e.printStackTrace();
	     	}
		 MessageResolver resolver = new PropertiesMessageResolver(props);
		 List<PasswordData.Reference> history = Arrays.asList(
				 // Password=P@ssword1
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "j93vuQDT5ZpZ5L9FxSfeh87zznS3CM8govlLNHU8GRWG/9LjUhtbFp7Jp1Z4yS7t"),

	                // Password=P@ssword2
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "mhR+BHzcQXt2fOUWCy4f903AHA6LzNYKlSOQ7r9np02G/9LjUhtbFp7Jp1Z4yS7t"),

	                // Password=P@ssword3
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "BDr/pEo1eMmJoeP6gRKh6QMmiGAyGcddvfAHH+VJ05iG/9LjUhtbFp7Jp1Z4yS7t")
	        );
	        EncodingHashBean hasher = new EncodingHashBean(
	                new CodecSpec("Base64"), // Handles base64 encoding
	                new DigestSpec("SHA256"), // Digest algorithm
	                1, // Number of hash rounds
	                false); // Salted hash == false

	        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(

	                // length between 8 and 16 characters
	                new LengthRule(8, 16),

	                // at least one upper-case character
	                new CharacterRule(EnglishCharacterData.UpperCase, 1),

	                // at least one lower-case character
	                new CharacterRule(EnglishCharacterData.LowerCase, 1),

	                // at least one digit character
	                new CharacterRule(EnglishCharacterData.Digit, 1),

	                // at least one symbol (special character)
	                new CharacterRule(EnglishCharacterData.Special, 1),

	                // no whitespace
	                new WhitespaceRule(),

	                // rejects passwords that contain a sequence of >= 3 characters alphabetical  (e.g. abc, ABC )
	                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
	                // rejects passwords that contain a sequence of >= 3 characters numerical   (e.g. 123)
	                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false)
	                ,new DigestHistoryRule(hasher)
	               ));

	        RuleResult result = validator.validate(new PasswordData(password));
	        PasswordData data = new PasswordData("P@ssword1", password);//"P@ssword1");
	        data.setPasswordReferences(history);
	        RuleResult result2 = validator.validate(data);

	        if (result.isValid() ) {
	            return true;
	        }
	        try {
	            if (result.isValid()==false) {
	                List<String> messages = validator.getMessages(result);

	                messageTemplate = String.join(",", messages);

	                System.out.println("Invalid Password: " + validator.getMessages(result));
	                }
	               } finally
			{
	            throw new PasswordValidException(messageTemplate);
	        }
	    }
	@Override
	public void lockUser(String username) {
		User u = userRepository.findByUsername(username).get();
		u.setLoginAttempts(0);
		u.setLocked(true);
		userRepository.save(u);
		Notification notif = new Notification();
		notif.setCreatedAt(new Date());
		notif.setMessage("Pour des raisons de sécurité "  + u.getUsername()+   " sera blocké!");
		notif.setRead(false);
		notif.setUser(u);
		notificationRepository.save(notif);

	}

	@Override
	@Transactional //Transactional is required when executing an update/delete query.
	public void makeAdmin(String username) {

		userRepository.makeAdmin(username);
		User u = userRepository.findByUsername(username).get();
		Notification notif = new Notification();
		notif.setCreatedAt(new Date());
		notif.setMessage("Félécitations "  + u.getUsername()+   " est notre nouveau admin!");
		notif.setRead(false);
		notif.setUser(u);
		notificationRepository.save(notif);

	}
	@Override
	@Transactional //Transactional is required when executing an update/delete query.
	public void changeRole(Role newRole, String username)
	{
		userRepository.updateUserRole(username, newRole);
	}
	@Override
	public List<User> allAdmins(){
		List<User> users = userRepository.findAll();
		List<User> result = new ArrayList<>();
		for (User u : users) {
			if (u.getRole().name().equals("ADMIN")) {
				result.add(u);
			}
		}
		return result;
	}

	@Override
	public List<Notification> findNotificationsByUser(Long userId) {
		return notificationRepository.userNotification(userId);
	}

	@Override
	public Notification addNotification(Notification notification, String username) {
		User user = userRepository.findByUsername(username).get();
		notification.setRead(false);
		notification.setUser(user);
		return notificationRepository.save(notification);
	}

	@Override
	public void deleteNotification(Long notificationId) {
		Notification notif = notificationRepository.findById(notificationId).orElse(null);
		notificationRepository.delete(notif);

	}

	@Override
	public List<Notification> findAllNotifications() {
		// TODO Auto-generated method stub
		return notificationRepository.findAll();
	}

	@Override
	public void markNotifAsRead(Long  idNotif) {
		Notification notification = notificationRepository.findById(idNotif).orElse(null);
		notification.setRead(true);
		notificationRepository.save(notification);

	}

	@Override
	public void markNotifAsUnRead(Long idNotif) {
		Notification notification = notificationRepository.findById(idNotif).orElse(null);
		notification.setRead(false);
		notificationRepository.save(notification);

	}
	@Override
	public List<User> findUsersByAgence(User user) {
		return userRepository.findByAgence(user.getAgence());
	}

	@Override
	public List<User> findAdminByNomAgence(String nomAgence) {
		return userRepository.findAdminByNomAgence(nomAgence, Role.ADMIN);
	}

	@Override
	public List<User> getUserByAgence(Long agenceId) {
		return userRepository.findByAgenceAgenceId(agenceId);
	}
}
