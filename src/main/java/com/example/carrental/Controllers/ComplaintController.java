package com.example.carrental.Controllers;



import com.example.carrental.Enumerations.ComplaintType;
import com.example.carrental.Models.Complaint;
import com.example.carrental.Models.User;
import com.example.carrental.ServiceInterfaces.ComplaintService;
import com.example.carrental.ServiceInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {


    @Autowired
    ComplaintService complaintService;

    @Autowired
    private UserService userService;



    @PostMapping("/AddComplaint/{userId}")
    public Complaint addComplaint(@RequestBody Complaint complaint, @PathVariable Long userId) {
        return complaintService.addComplaint(complaint, userId);
    }

    @DeleteMapping("/deleteComplaint/{ComplaintId}")
    public void deleteComplaint(@PathVariable("ComplaintId") Integer id) {
        complaintService.deleteComplaint(id);

    }

    @GetMapping("/retrieveAllComplaints")
    public List<Complaint> retrieveAllComplaints() {

        return complaintService.retrieveAllComplaints();
    }


    @PutMapping("/updateUntreatedComplaint/{CompalaintId}")
    @Transactional
    public Complaint updateComplaint2(@PathVariable(value ="CompalaintId")Integer id) throws MessagingException {
        return complaintService.updateComplaint2(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable int id) {
        Complaint complaint = complaintService.getComplaintById(id);
        return new ResponseEntity<>(complaint, HttpStatus.OK);
    }
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Complaint>> getComplaintsByType(@PathVariable("type") ComplaintType type) {
        List<Complaint> complaints = complaintService.getComplaintsByType(type);
        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }
}
