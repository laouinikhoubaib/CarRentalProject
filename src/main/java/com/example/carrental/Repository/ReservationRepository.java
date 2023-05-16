package com.example.carrental.Repository;


import com.example.carrental.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("select r from Reservation r WHERE r.datedebut between :datedebut and :datefin or r.datefin between :datedebut and :datefin or r.vehiculeReservation.dateajout>:datedebut")
    public List<Reservation> getReservationByDates(LocalDate datedebut, LocalDate datefin);
}
