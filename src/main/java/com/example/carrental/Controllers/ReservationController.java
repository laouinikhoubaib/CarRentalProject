package com.example.carrental.Controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reservation")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
}
