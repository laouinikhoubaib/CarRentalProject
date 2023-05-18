package com.example.carrental.Service;


import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Exceptions.NotFoundException;
import com.example.carrental.Models.Reservation;
import com.example.carrental.Repository.ReservationRepository;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.ServiceInterfaces.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private Session session;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VehiculeRepository vehiculeRepository;


    private ReservationDTO mapToDTO(final Reservation reservation,
                                       final ReservationDTO reservationDTO) {
        reservationDTO.setReservid(reservation.getReservid());
        reservationDTO.setDatedebut(reservation.getDatedebut());
        reservationDTO.setNbjour(reservation.getNbjour());
        reservationDTO.setReservationVehicule(reservation.getVehiculeReservation() == null ? null : reservation.getVehiculeReservation().getVehiculeId());
        reservationDTO.setUserReservation(Math.toIntExact(reservation.getUserReservation() == null ? null : reservation.getUserReservation().getUserId()));
        reservationDTO.setVehiculeReservation(reservation.getVehiculeReservation() == null ? null : reservation.getVehiculeReservation().getVehiculeId());
        return reservationDTO;
    }

    @Override
    public List<ReservationDTO> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll(Sort.by("reservid"));
        return reservations.stream()
                .map((reservation) -> mapToDTO(reservation, new ReservationDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO getById(final Integer reservid) {
        return reservationRepository.findById(reservid)
                .map(rentalContract -> mapToDTO(rentalContract, new ReservationDTO()))
                .orElseThrow(NotFoundException::new);
    }
    public boolean contractIsValid(Reservation reservation){
        List<Reservation> reservationList=reservationRepository.getReservationByDates(reservation.getDatedebut(),reservation.getDatefin());
        return reservationList.isEmpty();
    }

    @Override
    public boolean updateReservation(Reservation reservation) {
        if(reservationRepository.existsById(reservation.getReservid())){
            reservationRepository.save(reservation).equals(reservation);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteReservation(int id) {
        if(reservationRepository.existsById(id)){
            reservationRepository.deleteById(id);
            return true;
        }else{
            return  false;
        }
    }
}
