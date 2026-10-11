package com.eventify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventify.model.Attendee;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Integer> {

    Optional<Attendee> findByEmail(String email);
}