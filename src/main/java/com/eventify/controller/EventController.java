package com.eventify.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventify.model.Event;
import com.eventify.model.EventResponseDTO;
import com.eventify.service.EventMapper;
import com.eventify.service.EventService;

@RestController
@RequestMapping("/eventify/event")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;


    public EventController(
            EventService eventService,
            EventMapper eventMapper) {

        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }


    // =====================================================
    // GET ALL EVENTS
    // =====================================================

    @GetMapping(produces = "application/json")
    public List<EventResponseDTO> getAllEvents() {

        return eventService.getAllEvents()
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }


    // =====================================================
    // GET EVENT BY ID
    // =====================================================

    @GetMapping(
            value = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<EventResponseDTO> getEventById(
            @PathVariable Integer id) {

        return eventService
                .getEventById(id)
                .map(eventMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    // =====================================================
    // ADMIN - CREATE EVENT
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    public Event createEvent(
            @RequestBody Event event) {

        return eventService.createEvent(event);
    }


    // =====================================================
    // ADMIN - UPDATE EVENT
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(
            value = "/{id}",
            produces = "application/json",
            consumes = "application/json"
    )
    public Event updateEvent(
            @PathVariable Integer id,
            @RequestBody Event event) {

        return eventService
                .updateEvent(id, event);
    }


    // =====================================================
    // ADMIN - DELETE EVENT
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Integer id) {

        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }


    // =====================================================
    // ORGANIZER - GET OWN EVENTS
    // =====================================================

    @PreAuthorize("hasRole('ORGANIZER')")
    @GetMapping(
            "/organizer/{organizerid}"
    )
    public List<EventResponseDTO> getOrganizerEvents(
            @PathVariable Long organizerid) {

        return eventService
                .getEventsByOrganizer(
                        organizerid
                )
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }


    // =====================================================
    // ORGANIZER - CREATE EVENT
    // =====================================================

    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping(
            value = "/organizer/{organizerid}",
            produces = "application/json",
            consumes = "application/json"
    )
    public Event createOrganizerEvent(
            @PathVariable Long organizerid,
            @RequestBody Event event) {

        return eventService
                .createEventForOrganizer(
                        organizerid,
                        event
                );
    }


    // =====================================================
    // ORGANIZER - UPDATE OWN EVENT
    // =====================================================

    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping(
            value = "/organizer/{organizerid}/{eventId}",
            produces = "application/json",
            consumes = "application/json"
    )
    public Event updateOrganizerEvent(
            @PathVariable Long organizerid,
            @PathVariable Integer eventId,
            @RequestBody Event event) {

        return eventService
                .updateOrganizerEvent(
                        organizerid,
                        eventId,
                        event
                );
    }


    // =====================================================
    // ORGANIZER - CANCEL OWN EVENT
    // =====================================================

    @PreAuthorize("hasRole('ORGANIZER')")
    @PatchMapping(
            "/organizer/{organizerid}/{eventId}/cancel"
    )
    public ResponseEntity<Void> cancelOrganizerEvent(
            @PathVariable Long organizerid,
            @PathVariable Integer eventId) {

        eventService
                .cancelOrganizerEvent(
                        organizerid,
                        eventId
                );

        return ResponseEntity.noContent().build();
    }
}