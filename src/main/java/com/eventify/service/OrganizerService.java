package com.eventify.service;

import java.util.List;
import java.util.Optional;

import com.eventify.model.Organizer;

public interface OrganizerService {

    List<Organizer> getAllOrganizers();

    Optional<Organizer> getOrganizerById(Long id);

    Organizer createOrganizer(Organizer organizer);

    Organizer updateOrganizer(
            Long id,
            Organizer updatedOrganizer
    );

    void deleteOrganizer(Long id);
}