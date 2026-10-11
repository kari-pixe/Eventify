package com.eventify.service;

import java.util.List;
import java.util.Optional;

import com.eventify.model.Registration;

public interface RegistrationService {

    Registration createRegistration(
            Integer attendeeId,
            Integer eventId,
            Integer ticketQuantity
    );

    List<Registration> getAllRegistrations();

    Optional<Registration> getRegistrationById(
            Integer registrationId
    );

    List<Registration> getRegistrationsByAttendee(
            Integer attendeeId
    );

    List<Registration> getRegistrationsByEvent(
            Integer eventId
    );

    Registration cancelRegistration(
            Integer registrationId
    );
}