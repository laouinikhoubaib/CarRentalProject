package com.example.carrental.ServiceInterfaces;

import com.example.carrental.Exceptions.*;
import com.example.carrental.Models.Agence;
import com.example.carrental.Models.User;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.jsonwebtoken.io.IOException;
import javax.mail.MessagingException;
import java.util.List;

public interface AgenceService {

    public Agence saveAgence(Agence agence) throws AgenceNotExist, AgenceExist, MessagingException, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException;

    List<Agence> getAgences();

    Agence getAgenceById(Long agenceId);

    public Agence updateAgence(Agence agence);

    void deleteAgence(Long agenceId);

    List<Agence> findAllAgences();



}
