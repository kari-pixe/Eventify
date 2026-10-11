package com.eventify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventify.model.Event;
import com.eventify.model.EventStatus;
import com.eventify.model.Organizer;
import com.eventify.repository.EventRepository;
import com.eventify.repository.OrganizerRepository;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;


    public EventServiceImpl(
            EventRepository eventRepository,
            OrganizerRepository organizerRepository) {

        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
    }


    // =====================================================
    // GET ALL EVENTS
    // =====================================================

    @Override
    public List<Event> getAllEvents() {

        return eventRepository.findAll();
    }


    // =====================================================
    // CREATE EVENT - GENERAL
    // =====================================================

    @Override
    public Event createEvent(Event event) {

        if (event.getStatus() == null) {
            event.setStatus(EventStatus.PENDING_APPROVAL);
        }

        return eventRepository.save(event);
    }


    // =====================================================
    // GET EVENT BY ID
    // =====================================================

    @Override
    public Optional<Event> getEventById(Integer id) {

        return eventRepository.findById(id);
    }

    // =====================================================
    // UPDATE EVENT - ADMIN
    // =====================================================

    @Override
    public Event updateEvent(
            Integer id,
            Event updatedEvent) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Event not found with ID: " + id
                        )
                );

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setCategory(updatedEvent.getCategory());
        event.setEventType(updatedEvent.getEventType());
        event.setLocation(updatedEvent.getLocation());
        event.setStartDate(updatedEvent.getStartDate());
        event.setEndDate(updatedEvent.getEndDate());
        event.setCapacity(updatedEvent.getCapacity());
        event.setPrice(updatedEvent.getPrice());

        if (updatedEvent.getStatus() != null) {
            event.setStatus(updatedEvent.getStatus());
        }

        return eventRepository.save(event);
    }


    // =====================================================
    // DELETE EVENT - ADMIN
    // =====================================================

    @Override
    public void deleteEvent(Integer id) {

        if (!eventRepository.existsById(id)) {

            throw new RuntimeException(
                    "Event not found with ID: " + id
            );
        }

        eventRepository.deleteById(id);
    }


    // =====================================================
    // GET ORGANIZER'S EVENTS
    // =====================================================

    @Override
    public List<Event> getEventsByOrganizer(
            Long organizerid) {

        if (!organizerRepository.existsById(organizerid)) {

            throw new RuntimeException(
                    "Organizer not found with ID: "
                            + organizerid
            );
        }

        return eventRepository
                .findByOrganizer_OrganizerId(organizerid);
    }


    // =====================================================
    // CREATE EVENT FOR ORGANIZER
    // =====================================================

    @Override
    public Event createEventForOrganizer(
            Long organizerid,
            Event event) {

        Organizer organizer = organizerRepository
                .findById(organizerid)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Organizer not found with ID: "
                                        + organizerid
                        )
                );

        // Set organizer relationship
        event.setOrganizer(organizer);

        // Default status
        if (event.getStatus() == null) {

            event.setStatus(
                    EventStatus.PENDING_APPROVAL
            );
        }

        return eventRepository.save(event);
    }


    // =====================================================
    // UPDATE ORGANIZER'S OWN EVENT
    // =====================================================

    @Override
    public Event updateOrganizerEvent(
            Long organizerid,
            Integer eventId,
            Event updatedEvent) {

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Event not found with ID: "
                                        + eventId
                        )
                );

        if (event.getOrganizer() == null ||
                !event.getOrganizer()
                        .getOrganizerId()
                        .equals(organizerid)) {

            throw new RuntimeException(
                    "You are not authorized to update this event"
            );
        }

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setCategory(updatedEvent.getCategory());
        event.setEventType(updatedEvent.getEventType());
        event.setLocation(updatedEvent.getLocation());
        event.setStartDate(updatedEvent.getStartDate());
        event.setEndDate(updatedEvent.getEndDate());
        event.setCapacity(updatedEvent.getCapacity());
        event.setPrice(updatedEvent.getPrice());

        return eventRepository.save(event);
    }


    // =====================================================
    // CANCEL ORGANIZER'S OWN EVENT
    // =====================================================

    @Override
    public void cancelOrganizerEvent(
            Long organizerid,
            Integer eventId) {

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Event not found with ID: "
                                        + eventId
                        )
                );

        if (event.getOrganizer() == null ||
                !event.getOrganizer()
                        .getOrganizerId()
                        .equals(organizerid)) {

            throw new RuntimeException(
                    "You are not authorized to cancel this event"
            );
        }

        event.setStatus(EventStatus.CANCELLED);

        eventRepository.save(event);
    }
    }
