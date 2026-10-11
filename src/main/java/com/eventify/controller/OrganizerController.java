package com.eventify.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventify.model.Organizer;
import com.eventify.service.OrganizerService;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {

    private final OrganizerService organizerService;

    public OrganizerController(
            OrganizerService organizerService) {

        this.organizerService = organizerService;
    }


    // Get all organizers
    @GetMapping
    public ResponseEntity<List<Organizer>> getAllOrganizers() {

        return ResponseEntity.ok(
                organizerService.getAllOrganizers()
        );
    }


    // Get organizer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Organizer> getOrganizerById(
            @PathVariable Long id) {

        return organizerService
                .getOrganizerById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    // Create organizer
    @PostMapping
    public ResponseEntity<Organizer> createOrganizer(
            @RequestBody Organizer organizer) {

        Organizer createdOrganizer =
                organizerService.createOrganizer(
                        organizer
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrganizer);
    }


    // Update organizer
    @PutMapping("/{id}")
    public ResponseEntity<Organizer> updateOrganizer(
            @PathVariable Long id,
            @RequestBody Organizer organizer) {

        Organizer updatedOrganizer =
                organizerService.updateOrganizer(
                        id,
                        organizer
                );

        return ResponseEntity.ok(
                updatedOrganizer
        );
    }


    // Delete organizer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizer(
            @PathVariable Long id) {

        organizerService.deleteOrganizer(id);

        return ResponseEntity.noContent().build();
    }
}