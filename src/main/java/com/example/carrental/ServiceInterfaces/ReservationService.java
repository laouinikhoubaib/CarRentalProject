package com.example.carrental.ServiceInterfaces;

import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Models.Reservation;

import java.util.List;

public interface ReservationService {

    public List<ReservationDTO> findAll();
    public ReservationDTO getById(final Integer reservid);
    boolean updateReservation(Reservation reservation);
    boolean deleteReservation(int id);
}
