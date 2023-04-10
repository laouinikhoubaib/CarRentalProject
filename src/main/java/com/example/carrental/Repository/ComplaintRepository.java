package com.example.carrental.Repository;

import com.example.carrental.Enumerations.ComplaintStatus;
import com.example.carrental.Enumerations.ComplaintType;
import com.example.carrental.Models.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByComplaintType(ComplaintType type);
}
