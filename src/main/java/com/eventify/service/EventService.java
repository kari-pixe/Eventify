package com.eventify.service;

import com.eventify.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEvents();

    Event createEvent(Event event);

    Optional<Event> getEventById(Integer id);

    Event updateEvent(Integer id, Event updatedEvent);

    void deleteEvent(Integer id);

    List<Event> getEventsByOrganizer(Long organizerId);

    Event createEventForOrganizer(
            Long organizerId,
            Event event
    );

    Event updateOrganizerEvent(
            Long organizerId,
            Integer eventId,
            Event updatedEvent
    );

    void cancelOrganizerEvent(
            Long organizerId,
            Integer eventId
    );
}