package com.eventify.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventify.model.Registration;
import com.eventify.service.RegistrationService;

@RestController
@RequestMapping("/eventify/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;


    public RegistrationController(
            RegistrationService registrationService) {

        this.registrationService =
                registrationService;
    }


    // Create registration
    @PostMapping
    public ResponseEntity<Registration>
    createRegistration(

            @RequestParam Integer attendeeId,

            @RequestParam Integer eventId,

            @RequestParam Integer ticketQuantity) {

        Registration registration =
                registrationService
                        .createRegistration(
                                attendeeId,
                                eventId,
                                ticketQuantity
                        );

        return new ResponseEntity<>(
                registration,
                HttpStatus.CREATED
        );
    }


    // Get all registrations
    @GetMapping
    public ResponseEntity<List<Registration>>
    getAllRegistrations() {

        return ResponseEntity.ok(
                registrationService
                        .getAllRegistrations()
        );
    }


    // Get registration by ID
    @GetMapping("/{id}")
    public ResponseEntity<Registration>
    getRegistrationById(
            @PathVariable Integer id) {

        return registrationService
                .getRegistrationById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound()
                                .build()
                );
    }


    // Get registrations of attendee
    @GetMapping("/attendee/{attendeeId}")
    public ResponseEntity<List<Registration>>
    getRegistrationsByAttendee(
            @PathVariable Integer attendeeId) {

        return ResponseEntity.ok(
                registrationService
                        .getRegistrationsByAttendee(
                                attendeeId
                        )
        );
    }


    // Get registrations for event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Registration>>
    getRegistrationsByEvent(
            @PathVariable Integer eventId) {

        return ResponseEntity.ok(
                registrationService
                        .getRegistrationsByEvent(
                                eventId
                        )
        );
    }


    // Cancel registration
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Registration>
    cancelRegistration(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                registrationService
                        .cancelRegistration(id)
        );
    }
}