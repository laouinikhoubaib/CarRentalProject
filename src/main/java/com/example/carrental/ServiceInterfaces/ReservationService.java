package com.example.carrental.ServiceInterfaces;

import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Models.Reservation;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface ReservationService {

    public List<ReservationDTO> findAll();
    public ReservationDTO getById(final Integer reservid);
    boolean updateReservation(Reservation reservation);
    boolean deleteReservation(int id);
    public int addReservation(Reservation reservation, long userId,int vehiculeId) throws MessagingException;
    public double getChiffreAffaireByUser(long userId);
    public void exportcontrat(int idReserv, String filePath) throws IOException;
    public void rappelFinContrat() throws MessagingException;
}
