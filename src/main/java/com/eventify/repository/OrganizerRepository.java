package com.eventify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventify.model.Organizer;

@Repository
public interface OrganizerRepository
        extends JpaRepository<Organizer, Long> {

    Optional<Organizer> findByEmail(String email);
}