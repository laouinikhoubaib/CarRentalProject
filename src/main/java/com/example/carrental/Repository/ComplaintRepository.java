package com.example.carrental.Repository;

import com.example.carrental.Enumerations.ComplaintStatus;
import com.example.carrental.Enumerations.ComplaintType;
import com.example.carrental.Models.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    @Query(value = "SELECT COUNT(*) FROM complaint WHERE complaint_status = :status", nativeQuery = true)
    int countByComplaintStatus(@Param("status") String status);

    @Query(value = "SELECT COUNT(*) FROM complaint WHERE complaint_type = :type", nativeQuery = true)
    int countByComplaintType(@Param("type") String type);

    @Query("SELECT c.complaintDate, COUNT(c) FROM Complaint c WHERE c.complaintDate BETWEEN ?1 AND ?2 GROUP BY c.complaintDate")
    List<Object[]> findComplaintsCountByDate(Date startDate, Date endDate);

}
