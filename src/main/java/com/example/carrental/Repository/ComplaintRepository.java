package com.example.carrental.Repository;

import com.example.carrental.Enumerations.ComplaintStatus;
import com.example.carrental.Models.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    @Query(value="select (*) from complaint c  join user u on u.user_id=c.user_user_id where c.complaint_status=:Status",nativeQuery=true)
    public List<Complaint> retrieveAllComplaintsByStatus(@Param("Status") ComplaintStatus Status);
    @Query(value="SELECT COUNT(*) from complaint c  join user u on u.user_id=c.user_user_id where c.complaint_status='untreated'",nativeQuery=true)
    public int  CountUntreatedComplaint();
}
