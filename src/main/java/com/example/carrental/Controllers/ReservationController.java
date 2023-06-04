package com.example.carrental.Controllers;


import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Models.Reservation;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.ReservationRepository;
import com.example.carrental.Service.UserServiceImpl;
import com.example.carrental.ServiceInterfaces.ReservationService;
import com.example.carrental.ServiceInterfaces.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/reservation")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserServiceImpl userService;
    @GetMapping("/GetAllReservations")
    public ResponseEntity<List<ReservationDTO>> getAllReservations(){
        List<ReservationDTO> reservationDTOS = reservationService.findAll();
        if(reservationDTOS.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(reservationDTOS);
        }
    }

    @GetMapping("/getReservation/{reservation}")
    public ResponseEntity<?> getReservation(@PathVariable("reservation") int reservation) {
        ReservationDTO reservationDTO = reservationService.getById(reservation);
        if (reservationDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(reservationDTO);
        }
    }

    @PostMapping("/addReservation/{vehiculeId}/{userId}")
    public ResponseEntity<Object> addReservation(@RequestBody Reservation reservation,
                                                 @PathVariable("vehiculeId") int vehiculeId,
                                                 @PathVariable("userId") Long userId) throws MessagingException {
        try {
            int contratId = reservationService.addReservation(reservation, vehiculeId, userId);
            if (contratId == -1) {
                throw new Exception();
            }
            return ResponseEntity.ok().body("{\"contrat\": " + contratId + "}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Une erreur s'est produite lors de l'ajout du contrat : " + e.getMessage());
        }
    }


    @GetMapping("/ContractIsValidd/{datedebut}/{datefin}")
    public Boolean contractIsValid(@PathVariable("datedebut") String datedebut,@PathVariable("datefin") String datefin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return reservationService.contractIsValidd(LocalDate.parse(datedebut,formatter),LocalDate.parse(datefin,formatter));
    }

    @GetMapping("/GetUsersFinContrat")
    public ResponseEntity<List<Integer>> getUsersFinContrat(){
        List<Integer> idUserFinContrat = reservationService.rappelFinContratAngular();
        if(idUserFinContrat.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(idUserFinContrat);
        }
    }

    @PutMapping("/updateReservation")
    public ResponseEntity<String> updateReservation(@RequestBody Reservation reservation) {
        boolean updated = reservationService.updateReservation(reservation);
        if (updated) {
            return ResponseEntity.ok("Contrat mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de mise à jour.");
        }
    }

    @DeleteMapping("/deleteReservation/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable("reservationId") int reservationId) {
        boolean deleted = reservationService.deleteReservation(reservationId);
        if (deleted) {
            return ResponseEntity.ok("Contrat  supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Contrat  introuvable.");
        }
    }

    @GetMapping("/revenue")
    public ResponseEntity<String> calculateRevenueForUser(@NonNull HttpServletRequest request) {
        Double revenue = reservationService.getChiffreAffaireByUser(request);
        User user = userService.getUserByToken(request);
        if (revenue != null) {
            return ResponseEntity.ok("Chiffre d'affaires pour l'utilisateur " + user.getUserId() + " : " + revenue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucun chiffre d'affaires n'a été trouvé pour l'utilisateur " + user.getUserId()+ ".");
        }
    }



    @PostMapping("/charge")
    public ResponseEntity<Charge> createCharge(@RequestParam("token") String token,
                                               @RequestParam("amount") int amount,
                                               @RequestParam("currency") String currency,
                                               @RequestParam ("id") int id
    ) throws StripeException
    {

        Charge charge = reservationService.createCharge(token,amount,currency,id);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
