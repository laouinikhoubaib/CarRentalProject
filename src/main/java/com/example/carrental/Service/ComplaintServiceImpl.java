package com.example.carrental.Service;

import com.example.carrental.Enumerations.ComplaintStatus;
import com.example.carrental.Enumerations.ComplaintType;
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
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public Complaint addComplaint(Complaint T, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User introuvable"));
        T.setUser(user);
        Complaint complaint = Crepo.save(T);
        Notification notif = new Notification();
        notif.setCreatedAt(new Date());
        notif.setMessage("Nouvelle réclamation est ajoutée !");
        notif.setRead(false);
        notificationRepository.save(notif);
        return Crepo.save(complaint);
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
    public Complaint updateComplaint2(Integer id) throws MessagingException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Complaint oldComplaint = Crepo.getOne(id);
        String email = oldComplaint.getUser().getEmail();
        String username = oldComplaint.getUser().getUsername();
        Date requestDate = oldComplaint.getComplaintDate();

        if (oldComplaint.isEtat() == false) {
            oldComplaint.setComplaint_status(ComplaintStatus.treated);
            oldComplaint.setEtat(true);


            emailService.sendSimpleMessage(email, "Votre Réclamation demandée Le " + requestDate ,
                    "Bonjour " + username + ", votre plainte est traitée avec succès à " + now, "/Users/khoubaib/Desktop/reclamation.PNG");
        }

        Notification notif = new Notification();
        Complaint c = Crepo.findById(id).orElse(null);
        notif.setCreatedAt(new Date());
        notif.setMessage("Réclamation " + c.getComplaintId()+ "  est  traitée !");
        notif.setRead(false);
        notificationRepository.save(notif);
        return Crepo.save(oldComplaint);


    }
    @Override
    public Complaint getComplaintById(int id) {
        Optional<Complaint> complaint = Crepo.findById(id);
        if (complaint.isPresent()) {
            return complaint.get();
        } else {
            throw new RuntimeException("Complaint not found with id: " + id);
        }
    }
    @Override
    public int countByComplaintStatus(String status) {
        return Crepo.countByComplaintStatus(status);
    }
    @Override
    public int countByComplaintType(String type) {
        return Crepo.countByComplaintType(type);
    }
    @Override
    public List<Integer> getComplaintsCountByDate() {
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = endDate.minusMonths(1);

        List<Object[]> complaintsByDate = Crepo.findComplaintsCountByDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<Integer> complaintsCount = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            complaintsCount.add(0);
        }
        for (Object[] complaint : complaintsByDate) {
            Date complaintDate = (Date) complaint[0];
            Long count = (Long) complaint[1];
            LocalDate localDate = complaintDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int dayOfMonth = localDate.getDayOfMonth();
            complaintsCount.set(dayOfMonth - 1, count.intValue());
        }

        return complaintsCount;
    }
}
