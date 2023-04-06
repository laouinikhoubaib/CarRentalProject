package com.example.carrental.ServiceInterfaces;

import com.example.carrental.Models.Complaint;

import javax.mail.MessagingException;
import java.util.List;

public interface ComplaintService {

    public Complaint addComplaint(Complaint T,Long userId);

    public void deleteComplaint(Integer id);

    public List<Complaint> retrieveAllComplaints();

    public void updateComplaint( Complaint newcomplaint, int idComplaint);

    public Complaint  updateComplaint2(Integer id, Long userId) throws MessagingException;
}
