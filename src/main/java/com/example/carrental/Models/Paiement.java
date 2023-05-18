package com.example.carrental.Models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cvc;

    @Column(nullable = false)
    private String cardholderName;
    @Column
    private Date datepaiement;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "paiement")
    private Reservation reservation;
}
