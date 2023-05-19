package com.example.carrental.Controllers;


import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Models.Reservation;
import com.example.carrental.Repository.ReservationRepository;
import com.example.carrental.ServiceInterfaces.ReservationService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("api/reservation")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

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
    public ResponseEntity<?> getRentalOffer(@PathVariable("reservation") int reservation) {
        ReservationDTO reservationDTO = reservationService.getById(reservation);
        if (reservationDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(reservationDTO);
        }
    }

    @PostMapping("/addReservation/{userId}/{vehiculeId}")
    public ResponseEntity<String> addReservation(@RequestBody Reservation reservation, @PathVariable("userId") int userId, @PathVariable("vehiculeId") int vehiculeId) throws MessagingException {
        try {
            int contratId = reservationService.addReservation(reservation, userId, vehiculeId);
            return ResponseEntity.ok("Le contrat a été ajouté avec succès. Identifiant de contrat : " + contratId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de l'ajout du contrat : " + e.getMessage());
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

    @DeleteMapping("/deleteReservation/{reservation}")
    public ResponseEntity<String> deleteReservation(@PathVariable("reservation") int reservation) {
        boolean deleted = reservationService.deleteReservation(reservation);
        if (deleted) {
            return ResponseEntity.ok("Contrat  supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Contrat  introuvable.");
        }
    }

    @GetMapping("/revenue/{userId}")
    public ResponseEntity<String> calculateRevenueForUser(@PathVariable int userId) {
        Double revenue = reservationService.getChiffreAffaireByUser(userId);
        if (revenue != null) {
            return ResponseEntity.ok("Chiffre d'affaires pour l'utilisateur " + userId + " : " + revenue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucun chiffre d'affaires n'a été trouvé pour l'utilisateur " + userId + ".");
        }
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<Resource> exportContrat(@PathVariable int id) throws IOException {

        String filename = "contrat_" + id + ".pdf";
        String filePath = "C:/Users/khoubaib/Desktop/" + filename;

        // Export the contract to PDF
        reservationService.exportcontrat(id, filePath);

        // Prepare the file as a Resource
        File file = new File(filePath);
        Path path = file.toPath();
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);

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
