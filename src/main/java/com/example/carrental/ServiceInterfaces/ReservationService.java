package com.example.carrental.ServiceInterfaces;

import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Models.Reservation;
import lombok.NonNull;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    public List<ReservationDTO> findAll();
    public ReservationDTO getById(final Integer reservid);
    boolean updateReservation(Reservation reservation);
    boolean deleteReservation(int id);
    public int addReservation(Reservation reservation, int vehiculeId,Long userId)throws MessagingException ;
    public double getChiffreAffaireByUser(@NonNull HttpServletRequest request) ;
    public boolean contractIsValid(Reservation reservation);
    public boolean contractIsValidd(LocalDate datedebut, LocalDate datefin);
    public List<Integer> rappelFinContratAngular();
    public byte[] genererFacturePDF(Reservation reservation) throws Exception;




}
