package com.eventify.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventify.model.Attendee;
import com.eventify.service.AttendeeService;

@RestController
@RequestMapping("/eventify/attendee")
public class AttendeeController {

    private final AttendeeService attendeeService;

    public AttendeeController(
            AttendeeService attendeeService) {

        this.attendeeService = attendeeService;
    }


   //get all attendee :-->for admin only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = "application/json")
    public List<Attendee> getAllAttendees() {

        return attendeeService.getAllAttendees();
    }


    //get attendee by id:-->for admin only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(
            value = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<Attendee> getAttendeeById(
            @PathVariable Integer id) {

        return attendeeService
                .getAttendeeById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    //create new attendee
    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    public Attendee createAttendee(
            @RequestBody Attendee attendee) {

        return attendeeService
                .createAttendee(attendee);
    }


    //update attendee
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(
            value = "/{id}",
            produces = "application/json",
            consumes = "application/json"
    )
    public Attendee updateAttendee(
            @PathVariable Integer id,
            @RequestBody Attendee attendee) {

        return attendeeService
                .updateAttendee(
                        id,
                        attendee
                );
    }

    //delete attendee:-->admin only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendee(
            @PathVariable Integer id) {

        attendeeService.deleteAttendee(id);

        return ResponseEntity.noContent().build();
    }
}