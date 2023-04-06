package com.example.carrental.Controllers;



import com.example.carrental.Models.Complaint;
import com.example.carrental.Models.User;
import com.example.carrental.ServiceInterfaces.ComplaintService;
import com.example.carrental.ServiceInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/complaint")
@CrossOrigin(origins = "http://localhost:4200")
public class ComplaintController {


    @Autowired
    ComplaintService complaintService;

    @Autowired
    private UserService userService;



    @PostMapping("/AddComplaint/{userId}")
    public Complaint addComplaint(@RequestBody Complaint complaint, @PathVariable Long userId) {
        return complaintService.addComplaint(complaint, userId);
    }

    @DeleteMapping("/deleteComplaint/{Complaint_id}")
    public void deleteComplaint(@PathVariable("Complaint_id") Integer id) {
        complaintService.deleteComplaint(id);

    }

    @PutMapping("/updateCompalaint/{Compalaint-id}")
    public void updateComplaint(@RequestBody Complaint newcomplaint, @PathVariable("Compalaint-id") int idComplaint) {
        complaintService.updateComplaint(newcomplaint, idComplaint);
    }

    @GetMapping("/retrieveAllComplaints")
    public List<Complaint> retrieveAllComplaints() {

        return complaintService.retrieveAllComplaints();
    }


    @PutMapping("traiter/{id}")
    public ResponseEntity<Complaint> updateComplaint2(@PathVariable Integer id, @RequestBody Complaint complaint, @RequestParam Long userId) throws MessagingException {
        Complaint updatedComplaint = complaintService.updateComplaint2(id, userId);
        return ResponseEntity.ok(updatedComplaint);
    }



}
