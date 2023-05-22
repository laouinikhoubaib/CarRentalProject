package com.example.carrental.ServiceInterfaces;

import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Models.Reservation;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.NonNull;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    public List<ReservationDTO> findAll();
    public ReservationDTO getById(final Integer reservid);
    boolean updateReservation(Reservation reservation);
    boolean deleteReservation(int id);
    public int addReservation(Reservation reservation, @NonNull HttpServletRequest request, int vehiculeId)throws MessagingException ;
    public double getChiffreAffaireByUser(@NonNull HttpServletRequest request) ;
    public boolean contractIsValid(Reservation reservation);
    public boolean contractIsValidd(LocalDate datedebut, LocalDate datefin);
    public List<Integer> rappelFinContratAngular();
    public Charge createCharge(String token, int amount, String currency, int idcontract) throws StripeException;
}
