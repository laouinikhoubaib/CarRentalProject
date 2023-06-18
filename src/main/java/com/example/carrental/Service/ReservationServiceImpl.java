package com.example.carrental.Service;


import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Exceptions.NotFoundException;
import com.example.carrental.Models.Paiement;
import com.example.carrental.Models.Reservation;
import com.example.carrental.Models.User;
import com.example.carrental.Models.Vehicule;
import com.example.carrental.Repository.ReservationRepository;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.ServiceInterfaces.ReservationService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
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

    @Autowired
    UserServiceImpl userService;

    private ReservationDTO mapToDTO(Reservation reservation, ReservationDTO reservationDTO) {
        reservationDTO.setReservid(reservation.getReservid());
        reservationDTO.setDatedebut(reservation.getDatedebut());
        reservationDTO.setDatefin(reservation.getDatefin());
        reservationDTO.setNbjour(reservation.getNbjour());
        reservationDTO.setPrix(reservation.getPrix());
        reservationDTO.setUserReservation(Math.toIntExact(reservation.getUserReservation().getUserId()));
        reservationDTO.setVehiculeReservation(reservation.getVehiculeReservation().getVehiculeId());
        reservationDTO.setReservationVehicule(reservation.getReservid());
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
    @Override
    public boolean contractIsValid(Reservation reservation){
        List<Reservation> reservationList=reservationRepository.getReservationByDates(reservation.getDatedebut(),reservation.getDatefin());
        return reservationList.isEmpty();
    }
    @Override
    public boolean contractIsValidd(LocalDate datedebut,LocalDate datefin){
        List<Reservation> rentalContractList=reservationRepository.
                getReservationByDates(datedebut,datefin);
        System.out.println(rentalContractList.isEmpty());
        return rentalContractList.isEmpty();
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

    @Override
    public int addReservation(Reservation reservation, int vehiculeId, Long userId) throws MessagingException {
        LocalDate datefin = reservation.getDatedebut().plusDays(reservation.getNbjour());
        reservation.setDatefin(datefin);
        if (!contractIsValid(reservation))
            return -1;
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return -1;
        }
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId).orElse(null);
        if (vehicule == null) {
            return -1;
        }
        Set<Reservation> reservationList = vehicule.getVehiculeReservationReservations();
        reservation.setVehiculeReservation(vehicule);
        reservation.setPrix(reservation.getVehiculeReservation().getPrix() * reservation.getNbjour());
        reservation.setUserReservation(user);

        return reservationRepository.save(reservation).getReservid();
    }


    @Override
    public double getChiffreAffaireByUser(@NonNull HttpServletRequest request) {
        List<Vehicule> vehiculeListByUser=new ArrayList<>();
        List<Vehicule> vehiculeList =vehiculeRepository.findAll();
        User user = userService.getUserByToken(request);
        for (Vehicule vehicule:vehiculeList) {
            if (vehicule.getUser().getUserId()==user.getUserId()){
                vehiculeListByUser.add(vehicule);
            }
        }
        List<Reservation> reservationListUser=new ArrayList<>();
        List<Reservation> reservationList=reservationRepository.findAll();
        for (Reservation reservation:reservationList){
            for (Vehicule vehicule:vehiculeListByUser){
                if (reservation.getVehiculeReservation().getVehiculeId()==vehicule.getVehiculeId()){
                    reservationListUser.add(reservation);
                }}
        }
        Double totalRevenue = 0.0;
        for (Reservation reservation : reservationListUser) {
            totalRevenue += reservation.getPrix();
        }
        return totalRevenue;
    }


    public List<ReservationDTO> getContratUnJour(@NonNull HttpServletRequest request){

        List<Reservation> reservationList= reservationRepository.findAll();
        LocalDate date = LocalDate.now();
        List<Reservation> reservationsEndDate=new ArrayList<>();

        for (Reservation r:reservationList){
            LocalDate avantTroisJoursDateFin =r.getDatefin().minusDays(1);
            User user = userService.getUserByToken(request);
            if (r.getUserReservation().getUserId()==user.getUserId()){
                if (
                        (avantTroisJoursDateFin != null && date.isEqual(avantTroisJoursDateFin)) ||
                                (avantTroisJoursDateFin != null&&date.isAfter(avantTroisJoursDateFin)&& date.isBefore(r.getDatefin()))
                ){
                    reservationsEndDate.add(r);
                }
            }
        }
        return(reservationsEndDate.stream()
                .map((rentalContract) -> mapToDTO(rentalContract, new ReservationDTO()  ))
                .collect(Collectors.toList()));
    }
    public void sendEmail(String to, String subject, String text, String htmlBody) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        // Create the plain text part of the message
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(text);

        // Create the HTML part of the message
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html");

        // Create a multipart message and add the text and HTML parts to it
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(htmlPart);

        // Set the content of the message to the multipart message
        message.setContent(multipart);

        // Send the message
        Transport.send(message);
    }


    @Override

    public List<Integer> rappelFinContratAngular() {

        List<Reservation> reservationList= reservationRepository.findAll();
        List<Reservation> reservationEndDate=new ArrayList<>();
        LocalDate date = LocalDate.now();
        List<User> userFinContrat=new ArrayList<>();
        List<Integer> idUserFinContrat=new ArrayList<>();

        for (Reservation r:reservationList){
            LocalDate avantUnJoursDateFin =r.getDatefin().minusDays(1);
            System.out.println("avantunJourDateFin"+avantUnJoursDateFin);
            System.out.println("local date"+date);

            if (
                    (avantUnJoursDateFin != null && date.isEqual(avantUnJoursDateFin)) ||
                            (avantUnJoursDateFin != null&&date.isAfter(avantUnJoursDateFin)&& date.isBefore(r.getDatefin()))
            ){
                reservationEndDate.add(r);
            }}
        System.out.println(reservationEndDate);
        for(Reservation r:reservationEndDate){
            if(!userFinContrat.contains(r))
                idUserFinContrat.add(Math.toIntExact(r.getUserReservation().getUserId()));
        }
        return  idUserFinContrat;

    }

    @Override
    public Charge createCharge(String token, int amount, String currency, int idreserv) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        Reservation reservation = reservationRepository.findById(idreserv).get();
        int id = reservation.getReservid();
        String username = reservation.getVehiculeReservation().getUser().getUsername();
        Paiement p = new Paiement();
        String secretKey = "sk_test_51N9A3rDi7Brh0eKhHdXEuTxdDeZXnOCr0sgitOHfaCSmo1MEkejhZOXTWlf4cvZO0oMdx4s7Qaa6dsFrcaUXayd4000DiU9dDP";
        p.setAmount(charge.getAmount());
        p.setCardNumber(secretKey);
        p.setCurrency(charge.getCurrency());
        p.setCardholderName(username);
        p.setCvc("test");
        reservation.setPaiement(p);
        p.setReservation(reservation);
        reservationRepository.save(reservation);
        return charge;
    }

}
