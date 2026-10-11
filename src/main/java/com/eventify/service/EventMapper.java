package com.eventify.service;

import org.springframework.stereotype.Component;

import com.eventify.model.Event;
import com.eventify.model.EventResponseDTO;
import com.eventify.model.Organizer;
import com.eventify.repository.RegistrationRepository;

@Component
public class EventMapper {

    private final RegistrationRepository registrationRepository;

    public EventMapper(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public EventResponseDTO toDto(Event event) {
        EventResponseDTO dto = new EventResponseDTO();

        dto.setId(event.getEventId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setShortDescription(truncate(event.getDescription(), 140));

        dto.setCategory(event.getCategory());
        dto.setType(event.getEventType());
        dto.setStatus(event.getStatus() != null ? event.getStatus().name() : null);

        if (event.getStartDate() != null) {
            dto.setDate(event.getStartDate().toLocalDate().toString());
            dto.setTime(String.format("%02d:%02d",
                    event.getStartDate().getHour(),
                    event.getStartDate().getMinute()));
        }

        dto.setLocation(event.getLocation());
        // No separate venue/address columns yet in the DB - location doubles as venue for now.
        dto.setVenue(event.getLocation());
        dto.setAddress(null);

        Organizer organizer = event.getOrganizer();
        if (organizer != null) {
            dto.setOrganizerId(organizer.getOrganizerId());
            dto.setOrganizerName(
                    organizer.getOrganizationName() != null
                            ? organizer.getOrganizationName()
                            : organizer.getName()
            );
        }

        dto.setPrice(event.getPrice());
        dto.setIsFree(event.getPrice() == null || event.getPrice() == 0);

        dto.setCapacity(event.getCapacity());

        Integer registered = event.getEventId() != null
                ? registrationRepository.sumActiveTicketQuantityByEvent(event.getEventId())
                : 0;
        dto.setRegisteredCount(registered != null ? registered : 0);

        // No image column yet - frontend falls back to a category-based placeholder.
        dto.setImage(null);

        return dto;
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return null;
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength).trim() + "...";
    }
}
