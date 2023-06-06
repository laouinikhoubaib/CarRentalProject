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
import java.math.BigInteger;
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User introuvable"));

        T.setUser(user);
        T.setComplaint_status(ComplaintStatus.non_traitee);
        Complaint savedComplaint = Crepo.save(T);

        Notification notification = new Notification();
        notification.setMessage("Une nouvelle plainte a été ajoutée.");
        notification.setCreatedAt(new Date());
        notification.setComplaint(savedComplaint);

        notificationRepository.save(notification);

        return savedComplaint;
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
            oldComplaint.setComplaint_status(ComplaintStatus.traitee);
            oldComplaint.setEtat(true);


            emailService.sendSimpleMessage(email, "Votre Réclamation demandée Le " + requestDate ,
                    "Bonjour " + username + ", votre plainte est traitée avec succès Le " + now, "/Users/khoubaib/Desktop/reclamation.JPG");
        }
        User user = userRepository.findByUsername(username).get();
        Notification notif = new Notification();
        Complaint c = Crepo.findById(id).orElse(null);
        notif.setCreatedAt(new Date());
        notif.setMessage("Votre réclamation a été  traitée !");
        notif.setRead(false);
        notif.setUser(user);
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
    public Map<String, Integer> getComplaintsByDayInCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        cal.setTime(currentDate);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        cal.set(currentYear, currentMonth - 1, 1, 0, 0, 0);
        Date startDate = cal.getTime();
        int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(currentYear, currentMonth - 1, lastDayOfMonth, 23, 59, 59);
        Date endDate = cal.getTime();
        List<Object[]> complaintsByDay = Crepo.getComplaintsByDayInMonth(startDate, endDate);
        Map<String, Integer> complaintsByDayMap = new LinkedHashMap<>();
        for (Object[] complaint : complaintsByDay) {
            String dayOfMonth = complaint[0].toString();
            BigInteger count = (BigInteger) complaint[1];
            complaintsByDayMap.put(dayOfMonth, count.intValue());
        }
        return complaintsByDayMap;
    }
}
