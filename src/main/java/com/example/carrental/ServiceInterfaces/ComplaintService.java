package com.example.carrental.ServiceInterfaces;

import com.example.carrental.Models.Complaint;

import javax.mail.MessagingException;
import java.util.List;

public interface ComplaintService {

    public Complaint addComplaint(Complaint T);

    public void deleteComplaint(Integer id);

    public List<Complaint> retrieveAllComplaints();

    public void affectatComplaintToUser(int Complaint_id, long id);

    public void updateComplaint( Complaint newcomplaint, int idComplaint);

    public Complaint  updateComplaint2(Integer id) throws MessagingException;
}
