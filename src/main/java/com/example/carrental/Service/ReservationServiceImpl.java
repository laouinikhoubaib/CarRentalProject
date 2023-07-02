package com.example.carrental.Service;


import com.example.carrental.DTO.ReservationDTO;
import com.example.carrental.Exceptions.NotFoundException;
import com.example.carrental.Models.*;
import com.example.carrental.Repository.ReservationRepository;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.Repository.VehiculeRepository;
import com.example.carrental.ServiceInterfaces.ReservationService;
import com.stripe.model.Charge;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.xhtmlrenderer.pdf.ITextRenderer;

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

    @Autowired
    private Configuration freemarkerConfig;

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
    public byte[] genererFacturePDF(Reservation reservation) throws Exception {

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(genererContenuHTML(reservation));
        renderer.layout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        Path fichierFacture = Paths.get("C:\\Users\\khoubaib\\Desktop\\" + "facture"+reservation.getReservid()+".pdf");
        Files.write(fichierFacture, outputStream.toByteArray());
        return outputStream.toByteArray();
    }

    private String genererContenuHTML(Reservation reservation) throws Exception {
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        freemarkerConfig.setDefaultEncoding("UTF-8");
        Map<String, Object> model = new HashMap<>();
        model.put("facture", reservation);
        Template template = freemarkerConfig.getTemplate("facture.html");

        StringWriter writer = new StringWriter();
        template.process(model, writer);
        String contenuHTML = writer.toString();

        return contenuHTML;
    }


}