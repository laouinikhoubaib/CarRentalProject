package com.example.carrental.Models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Reservation {


    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservid;

    @Column
    private LocalDate datedebut;
    @Column
    private LocalDate datefin;

    @Column
    private Integer nbjour;

    @Column
    private double prix;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_reservation_id")
//    private User userReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_vehiculereserv_reservation_id")
    private Vehicule vehiculeReservation;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "paiement")
    private Paiement paiement; //
}
