package com.example.carrental.Controllers;


import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Models.Reservation;
import com.example.carrental.Models.User;
import com.example.carrental.Repository.ReservationRepository;
import com.example.carrental.Service.UserServiceImpl;
import com.example.carrental.ServiceInterfaces.ReservationService;
import java.net.http.HttpRequest;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
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

            // Générer le QR code
            String text = reservation.getUserReservation().getUsername() + reservation.getPrix()
                    + reservation.getReservid() + reservation.getDatedebut()
                    + reservation.getDatefin() + reservation.getNbjour();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://codzz-qr-cods.p.rapidapi.com/getQrcode?type=text&value=" + text + ""))
                    .header("x-rapidapi-host", "codzz-qr-cods.p.rapidapi.com")
                    .header("x-rapidapi-key", "4505c1692bmsh4fe202f07557d6dp115480jsnac985346e260")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.err.println(response.body());
            reservation.setQrcode(response.body().substring(8, 61));
            reservationRepository.saveAndFlush(reservation);

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



    @PostMapping(path = "facture/{reservid}")
    public ResponseEntity<byte[]> factureReservation(@PathVariable("reservid") Long reservid) {
        try {
            Reservation reservation = reservationRepository.getById(reservid.intValue());
            byte[] contenuPDF = reservationService.genererFacturePDF(reservation);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename("facture.pdf").build());
            return new ResponseEntity<>(contenuPDF, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
//    @GetMapping("/qrcode/{reservid}")
//    public void takeYourPdfDonation(@PathVariable("reservid") Long reservid) throws IOException, InterruptedException {
//
//        Reservation reservation = reservationRepository.getById(reservid.intValue());
//         String text= reservation.getUserReservation().getUsername()+reservation.getPrix()+reservation.getReservid()+reservation.getDatedebut()
//                +reservation.getDatefin()+reservation.getNbjour();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://codzz-qr-cods.p.rapidapi.com/getQrcode?type=text&value="+text+""))
//                .header("x-rapidapi-host", "codzz-qr-cods.p.rapidapi.com")
//                .header("x-rapidapi-key", "4505c1692bmsh4fe202f07557d6dp115480jsnac985346e260")
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        System.err.println(response.body());
//        reservation.setQrcode(response.body().substring(8, 61));
//        reservationRepository.saveAndFlush(reservation);
//
//
//    }

}
