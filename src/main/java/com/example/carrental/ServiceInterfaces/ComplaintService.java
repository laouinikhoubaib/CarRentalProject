package com.example.carrental.ServiceInterfaces;

import com.example.carrental.Enumerations.ComplaintStatus;
import com.example.carrental.Enumerations.ComplaintType;
import com.example.carrental.Models.Complaint;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface ComplaintService {

    public Complaint addComplaint(Complaint T,Long userId);

    public void deleteComplaint(Integer id);

    public List<Complaint> retrieveAllComplaints();


    public Complaint  updateComplaint2(Integer id) throws MessagingException;

    public Complaint getComplaintById(int id);

    public int countByComplaintStatus(String status);

    public int countByComplaintType(String type);

    public List<Integer> getComplaintsCountByDate();
}
