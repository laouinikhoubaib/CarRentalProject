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
import com.itextpdf.text.DocumentException;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @Override
    public int addReservation(Reservation reservation, long userId,int vehiculeId) throws MessagingException {
        LocalDate datefin=reservation.getDatedebut().plusDays(reservation.getNbjour());
        reservation.setDatefin(datefin); //generation automatique de ladate fin

        if (!contractIsValid(reservation))
            return  -1;
        User user=userRepository.findById(userId).get();
        Vehicule vehicule=vehiculeRepository.findById(vehiculeId).get();
        Set<Reservation> reservationList=vehicule.getVehiculeReservationReservations();
        reservation.setUserReservation(user);
        reservation.setVehiculeReservation(vehicule);
        reservation.setPrix(reservation.getVehiculeReservation().getJourslocation()*reservation.getNbjour());

        return reservationRepository.save(reservation).getReservid();
    }

    @Override
    public double getChiffreAffaireByUser(long userId) {
        List<Vehicule> vehiculeListByUser=new ArrayList<>();
        List<Vehicule> vehiculeList =vehiculeRepository.findAll();
        for (Vehicule vehicule:vehiculeList) {
            if (vehicule.getUser().getUserId()==userId){
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
    public void exportcontrat(int idReserv, String filePath) throws IOException {
        try {
            Reservation reservation = reservationRepository.findById(idReserv).get();

            String htmlContent = "<!DOCTYPE html> \n" +
                    "<html>\n" +
                    "    <head><style>body{background-color: rgb(185, 161, 73);}h1{text-align: center ;font-weight: bold;color: rgb(255, 255, 255 );font-family: cursive;}p{text-align: center;font-size: medium;} </style></head>\n" +
                    "    <body>\n" +
                    "      <h1>Tech Master</h1> \n \n" +
                    "      \n" +
                    "      <p>Date début Contrat: "+ reservation.getDatedebut() +"</p>\n" +
                    "      <p>Date fin  Contrat: "+ reservation.getDatefin() +"</p>\n" +

                    "      <p>Prix: "+reservation.getPrix() +"</p>\n" +
                    "      <p>NomClient: "+ reservation.getUserReservation().getUsername()+" </p>\n" +
                    "      <p>Matricule de  vehicule: "+  reservation.getVehiculeReservation().getMatricule() +"</p>\n" +
                    "      <p>Date: "+new Date() +"</p>\n" +
                    "   \n" +
                    "    </body>\n" +
                    "</html>";

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));

            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
            worker.parseXHtml(writer, document, inputStream);

            document.close();
        } catch (DocumentException e) {
            // Handle DocumentException
        }
    }

    @Override
    @Scheduled(cron = "0 0 10 * * *")

    public void rappelFinContrat() throws MessagingException {

        List<Reservation> reservationList= reservationRepository.findAll();
        List<Reservation> reservationDateFin=new ArrayList<>();
        LocalDate date = LocalDate.now();

        for (Reservation r:reservationList){
            LocalDate avantTroisJoursDateFin =r.getDatefin().minusDays(1);
            if (
                    (avantTroisJoursDateFin != null && date.isEqual(avantTroisJoursDateFin)) ||
                            (avantTroisJoursDateFin != null&&date.isAfter(avantTroisJoursDateFin)&& date.isBefore(r.getDatefin()))
            ){
                reservationDateFin.add(r);
                System.out.println("Ce contrat est d'id :" + r.getReservid());
                User user=userRepository.findById(r.getUserReservation().getUserId()).get();
                String email= user.getEmail();
                System.out.println(email);
                String msg="Je vous rappel que la fin de votre contrat est le :"+r.getDatefin();
                String htmlBody = "<!DOCTYPE html> \n" +
                        "<html>\n" +
                        "    <head><style> header{background-color: (185, 161, 73); font-weight: bold;font-family: cursive ; text-align: center;}div{background-color: beige;}</style></head>\n" +
                        "    <body>\n" +
                        "       <header>TechMaster</header>\n" +
                        "          <div>\n" +
                        "          <h3>Bonjour </h3>" + user.getUsername()+
                        "           <a>On vous rappel que la fin de votre contrat </a> \n" + r.getReservid()+
                        "           <a> aura lieu le :</a>  \n" +r.getDatefin()+
                        "<footer>Bonne journée.</footer>    \n" +
                        "          </div>\n" +
                        "       \n" +
                        "    </body>\n" +
                        "</html>";
                sendEmail(email,"Rappel Fin Contrat", "", htmlBody);

            }}
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
