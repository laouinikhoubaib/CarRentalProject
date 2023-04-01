package com.example.carrental.ServiceInterfaces;

import com.example.carrental.Enumerations.TypeAgence;
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

    public Agence addAgence(Agence agence);

    List<Agence> getAgences();

    Agence getAgenceById(Long agenceId);

    public Agence updateAgence(Agence agence);

    void deleteAgence(Long agenceId);

    List<Agence> findAllAgences();

    public Agence getAgenceByNom(String nom);


    public List<Agence> findByTypeAgence(TypeAgence typeAgence);

}
