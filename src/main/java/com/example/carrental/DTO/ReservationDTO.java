package com.example.carrental.DTO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDTO {

    private Integer reservid;
    private LocalDate datedebut;
    private LocalDate datefin;
    private Integer nbjour;
    private double prix;

    private Integer userReservation;
    private Integer vehiculeReservation;
    private Integer reservationVehicule;

}
