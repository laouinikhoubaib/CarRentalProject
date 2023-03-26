package com.example.carrental.Service;

import com.example.carrental.Models.User;
import com.example.carrental.Repository.UserRepository;
import com.sun.mail.smtp.SMTPTransport;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;


@Service
public class ServiceAllEmail {
	
	@Autowired
	Configuration configuration;

    @Autowired
    UserRepository userRepository;
	
	//private JavaMailSender javaMailSender;
	
    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
        Message message = createEmail(firstName, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport("smtps");
        smtpTransport.connect("smtp.gmail.com", "laouinikhoubaib@gmail.com", "mqvqasngmgaekpaq");
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(String firstName, String password, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress("laouinikhoubaib@gmail.com"));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        //message.setRecipients(CC, InternetAddress.parse("bdtcourse@gmail.com", false));
        message.setSubject("Carz - New Password");
        message.setText("Hello " + firstName + ", \n \n Your new account password is: " + password + "\n \n The Support Team"+"\n From Les Elites Dev Team");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.starttls.required", true);
        return Session.getInstance(properties, null);
    }

    
    
    //Reset Password Email -----------------------------------------------------------------------------------
    private MimeMessage createresetPasswordMail(String token, String email) throws MessagingException, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException {
    	User user = userRepository.findByEmail(email).orElse(null);
        MimeMessage message = new MimeMessage(getEmailSession());
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(new InternetAddress("laouinikhoubaib@gmail.com"));
        helper.setTo(email);
        helper.setCc("khoubaib.laouini@esprit.tn");
        helper.setSubject("carz - Password Reset");
        String emailContent = getResetEmailContent(token);
        helper.setText(emailContent, true);
        helper.setSentDate(new Date());
        return message;
    }

    public void sendNewResetPasswordMail(String token, String email) throws MessagingException, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException {
        Message message = createresetPasswordMail(token, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport("smtps");
        smtpTransport.connect("smtp.gmail.com", "laouinikhoubaib@gmail.com", "mqvqasngmgaekpaq");
        smtpTransport.sendMessage(message, message.getAllRecipients());	
        smtpTransport.close();
    }

 
    String getResetEmailContent(String token) throws IOException, TemplateException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, java.io.IOException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap();
        model.put("token", token);
        configuration.getTemplate("ResetEmail.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
    //Reset Password Email -----------------------------------------------------------------------------------
    
    
    //Welcome Email -----------------------------------------------------------------------------------
    private MimeMessage createWelcomeMail(String token, String email) throws MessagingException, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException {
    	User user = userRepository.findByEmail(email).orElse(null);
        MimeMessage message = new MimeMessage(getEmailSession());
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(new InternetAddress("laouinikhoubaib@gmail.com"));
        helper.setTo(email);
        helper.setCc("khoubaib.laouini@esprit.tn");
        helper.setSubject("Welcome to Carz App");
        String emailContent = getWelcomeEmailContent(user);
        helper.setText(emailContent, true);
        helper.setSentDate(new Date());
        return message;
    }

    public void sendWelcomeMail(String token, String email) throws MessagingException, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException {
        Message message = createWelcomeMail(token, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport("smtps");
        smtpTransport.connect("smtp.gmail.com", "laouinikhoubaib@gmail.com", "mqvqasngmgaekpaq");
        smtpTransport.sendMessage(message, message.getAllRecipients());	
        smtpTransport.close();
    }
    
    String getWelcomeEmailContent(User user) throws IOException, TemplateException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, java.io.IOException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap();
        model.put("user", user);
        configuration.getTemplate("WelcomeEmail.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }








}
