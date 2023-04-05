package com.example.carrental.Service;

import com.example.carrental.Enumerations.ComplaintStatus;
import com.example.carrental.Models.Complaint;
import com.example.carrental.Models.Notification;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.ComplaintRepository;
import com.example.carrental.Repository.NotificationRepository;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.ServiceInterfaces.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    ComplaintRepository Crepo;

    @Autowired
    UserRepository ur;

    @Autowired
    ServiceAllEmail emailService;

    @Autowired
    NotificationRepository notificationRepository;


    @Override
    public Complaint addComplaint(Complaint T) {
        Complaint complaint = Crepo.save(T);
        Notification notif = new Notification();
        notif.setCreatedAt(new Date());
        notif.setMessage("Réclamation " + complaint.getComplaint_id()+ "  est  ajoutée avec succès !");
        notif.setRead(false);
        notificationRepository.save(notif);
        return complaint;
    }

    @Override
    public void deleteComplaint(Integer id) {

        Crepo.deleteById(id);
    }


    @Override
    public List<Complaint> retrieveAllComplaints() {

        return  (List<Complaint>) Crepo.findAll();
    }

    @Override
    public void updateComplaint(Complaint newcomplaint, int idComplaint) {
        Complaint c = Crepo.findById(idComplaint).orElse(null);
        c.setComplaintDate(newcomplaint.getComplaintDate());
        c.setDescription(newcomplaint.getDescription());
        c.setName(newcomplaint.getName());
        Crepo.save(c);
        Notification notif = new Notification();
        notif.setCreatedAt(new Date());
        notif.setMessage("Réclamation " + c.getComplaint_id()+ "  est  traitée !");
        notif.setRead(false);
        notificationRepository.save(notif);

    }

    @Override
    public Complaint updateComplaint2(Integer id) throws MessagingException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Complaint oldComplaint = Crepo.getOne(id);

        String Email=oldComplaint.getUser().getEmail();
        String fname=oldComplaint.getUser().getUsername();
        Date requestDate= (Date) oldComplaint.getComplaintDate();
        if (oldComplaint.isEtat()==false)
        {
            oldComplaint.setComplaint_status(ComplaintStatus.treated);
            oldComplaint.setEtat(true);
            emailService.sendSimpleMessage(Email, "Enregistrez votre Réclamation demandée sur "+requestDate+""+ "sous id" +oldComplaint.getComplaint_id(),
                    "Bonjour " + " " +fname+ "" +"votre plainte est traitée avec succès à"+""+now,"/Users/khoubaib/Desktop/khoubaib.jpg");
        }

        return Crepo.save(oldComplaint);
        
    }
    public void affectatComplaintToUser(int Complaint_id, long id) {
        User user=ur.findById(id).get();
        Complaint complaint=Crepo.findById(Complaint_id).get();
        complaint.setUser(user);
        Crepo.save(complaint);

    }
}
