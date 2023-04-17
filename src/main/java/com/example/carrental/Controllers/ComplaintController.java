package com.example.carrental.Controllers;



import com.example.carrental.Enumerations.ComplaintStatus;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {


    @Autowired
    ComplaintService complaintService;

    @Autowired
    private UserService userService;



    @PostMapping("/AddComplaint/{userId}")
    public ResponseEntity<Complaint> addComplaint(@RequestBody Complaint complaint, @PathVariable Long userId) {
        return new ResponseEntity<>(complaintService.addComplaint(complaint, userId), HttpStatus.CREATED);
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
    @GetMapping("/stats")
    public Map<String, Integer> getComplaintStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("treated", complaintService.countByComplaintStatus("treated"));
        stats.put("untreated", complaintService.countByComplaintStatus("untreated"));
        stats.put("InProgress", complaintService.countByComplaintStatus("InProgress"));
        return stats;
    }

    @GetMapping("/count-by-type")
    public Map<String, Integer> getCountByComplaintType() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("technical", complaintService.countByComplaintType("technical"));
        stats.put("service", complaintService.countByComplaintType("service"));
        return stats;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Integer>> getComplaintsByDayInCurrentMonth() {
        Map<String, Integer> complaintsByDay = complaintService.getComplaintsByDayInCurrentMonth();
        return ResponseEntity.ok(complaintsByDay);
    }

}
