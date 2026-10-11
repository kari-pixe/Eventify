package com.eventify.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventify.model.AccountStatus;
import com.eventify.model.Organizer;
import com.eventify.repository.OrganizerRepository;

@Service
public class OrganizerServiceImpl
        implements OrganizerService {

    private final OrganizerRepository organizerRepository;


    public OrganizerServiceImpl(
            OrganizerRepository organizerRepository) {

        this.organizerRepository =
                organizerRepository;
    }


    // Get all organizers
    @Override
    public List<Organizer> getAllOrganizers() {

        return organizerRepository.findAll();
    }


    // Get organizer by ID
    @Override
    public Optional<Organizer> getOrganizerById(
            Long id) {

        return organizerRepository.findById(id);
    }


    // Create organizer
    @Override
    public Organizer createOrganizer(
            Organizer organizer) {

        // Set default account status
        if (organizer.getAccountStatus() == null) {

            organizer.setAccountStatus(
                    AccountStatus.ACTIVE
            );
        }


        // Set creation date
        if (organizer.getCreatedAt() == null) {

            organizer.setCreatedAt(
                    LocalDateTime.now()
            );
        }


        return organizerRepository.save(
                organizer
        );
    }


    // Update organizer
    @Override
    public Organizer updateOrganizer(
            Long id,
            Organizer updatedOrganizer) {

        Organizer organizer =
                organizerRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organizer not found with ID: "
                                                + id
                                )
                        );


        organizer.setName(
                updatedOrganizer.getName()
        );

        organizer.setEmail(
                updatedOrganizer.getEmail()
        );

        organizer.setPhone(
                updatedOrganizer.getPhone()
        );

        organizer.setOrganizationName(
                updatedOrganizer.getOrganizationName()
        );


        return organizerRepository.save(
                organizer
        );
    }


    // Delete organizer
    @Override
    public void deleteOrganizer(
            Long id) {

        if (!organizerRepository.existsById(id)) {

            throw new RuntimeException(
                    "Organizer not found with ID: "
                            + id
            );
        }


        organizerRepository.deleteById(id);
    }
}