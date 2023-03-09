package com.example.carrental.Controllers;


import com.example.carrental.Exceptions.*;
import com.example.carrental.Models.Media;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.MediaRepo;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.ServiceInterfaces.AuthenticationService;
import com.example.carrental.ServiceInterfaces.JwtRefreshTokenService;
import com.example.carrental.ServiceInterfaces.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("api/authentication")
public class AuthenticationController {


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    MediaRepo mediaRepository;

    @Autowired
    UserRepository<User, Number> userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;


    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads/";
    public static String uploadDirectory22 = "C:\\Users\\khoubaib\\Desktop\\PFE\\uploads";
   public static String uploadDirectory2 = "C:\\Users\\khoubaib\\Desktop\\PFE\\upload";

    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @PostMapping(value="sign-up", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})//api/authentication/sign-up
    public ResponseEntity<User> signUp(@RequestPart("user") String user, @RequestPart("file") MultipartFile file) throws UsernameNotExist, UsernameExist, EmailExist, MessagingException, IOException, io.jsonwebtoken.io.IOException, TemplateException
    {
        //upload file

        File convertFile = new File(uploadDirectory+file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();
        Media profilPicture = new Media();
        profilPicture.setImagenUrl(uploadDirectory2+file.getOriginalFilename());
        profilPicture = mediaRepository.save(profilPicture);
        User userData = objectMapper.readValue(user, User.class);
        userData.setProfilPicture(profilPicture);
        userData.setProfilPic(file.getOriginalFilename());
        userService.saveUser(userData);
        return new ResponseEntity<>(userData, HttpStatus.CREATED);
    }

    @PostMapping("sign-in")//api/authentication/sign-in
    public ResponseEntity<?> signIn(@RequestBody User user) throws com.example.carrental.Exceptions.AccountLockedException
    {
        User u = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (u != null) {
            if (!u.isBlock()) {
                if (u.getLoginAttempts() != 4 && !passwordEncoder.matches(user.getPassword(), u.getPassword())){
                    u.setLoginAttempts(u.getLoginAttempts() + 1);
                    userRepository.save(u);
                    if (u.getLoginAttempts()==4) {
                        u.setBlock(true);
                        userRepository.save(u);
                        throw new com.example.carrental.Exceptions.AccountLockedException("Your account has been locked, please contact the administration !");
                    }
                    else {
                        return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
                    }
                }
                else if (u.getLoginAttempts() != 4 && passwordEncoder.matches(user.getPassword(), u.getPassword())){
                    u.setLoginAttempts(0);
                    userRepository.save(u);
                    return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
                }
                else {
                    throw new com.example.carrental.Exceptions.AccountLockedException("Your account has been locked, please contact the administration !");
                }
            }
            else {
                throw new com.example.carrental.Exceptions.AccountLockedException("Your account has been locked, please contact the administration !");
            }
        }
        else {
            return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
        }

    }

    @PostMapping("refresh-token")//api/authentication/refresh-token?token=
    public ResponseEntity<?> refreshToken(@RequestParam String token)
    {
        return ResponseEntity.ok(jwtRefreshTokenService.generateAccessTokenFromRefreshToken(token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> generatePasswordResetToken(@RequestParam String email) throws EmailNotExist, io.jsonwebtoken.io.IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException {
        return new ResponseEntity<>(authenticationService.generatePasswordResetToken(email), HttpStatus.OK);
    }

    @PostMapping("/reset-password/new")
    public ResponseEntity<?> updatePassword(@RequestParam String token, @RequestBody String newPassword) throws ResetPasswordException, ResetPasswordTokenException{
        authenticationService.updatePassword(token, newPassword);
        return new ResponseEntity<>("Your password has been successfully updated !", HttpStatus.OK);
    }
}
