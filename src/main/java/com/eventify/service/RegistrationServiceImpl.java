package com.eventify.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventify.model.Attendee;
import com.eventify.model.Event;
import com.eventify.model.EventStatus;
import com.eventify.model.Registration;
import com.eventify.model.RegistrationStatus;
import com.eventify.repository.AttendeeRepository;
import com.eventify.repository.EventRepository;
import com.eventify.repository.RegistrationRepository;

@Service
public class RegistrationServiceImpl
        implements RegistrationService {

    private final RegistrationRepository registrationRepository;

    private final AttendeeRepository attendeeRepository;

    private final EventRepository eventRepository;


    public RegistrationServiceImpl(
            RegistrationRepository registrationRepository,
            AttendeeRepository attendeeRepository,
            EventRepository eventRepository) {

        this.registrationRepository =
                registrationRepository;

        this.attendeeRepository =
                attendeeRepository;

        this.eventRepository =
                eventRepository;
    }


    // Create new registration
    @Override
    public Registration createRegistration(
            Integer attendeeId,
            Integer eventId,
            Integer ticketQuantity) {

        // Find attendee
        Attendee attendee =
                attendeeRepository
                        .findById(attendeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attendee not found with ID: "
                                                + attendeeId
                                )
                        );


        // Find event
        Event event =
                eventRepository
                        .findById(eventId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Event not found with ID: "
                                                + eventId
                                )
                        );


        // Validate ticket quantity
        if (ticketQuantity == null ||
                ticketQuantity <= 0) {

            throw new RuntimeException(
                    "Ticket quantity must be greater than zero"
            );
        }


        if (event.getStatus() != EventStatus.PUBLISHED) {

            throw new RuntimeException(
                    "Registration is available only for published events"
            );
        }

        // Check event capacity
        if (event.getCapacity() != null) {

            List<Registration> registrations =
                    registrationRepository
                            .findByEventEventId(eventId);


            int registeredTickets =
                    registrations
                            .stream()
                            .filter(registration ->
                                    registration.getStatus()
                                            == RegistrationStatus.CONFIRMED)
                            .mapToInt(
                                    Registration::getTicketQuantity
                            )
                            .sum();


            if (registeredTickets
                    + ticketQuantity
                    > event.getCapacity()) {

                throw new RuntimeException(
                        "Not enough seats available"
                );
            }
        }


        // Create registration
        Registration registration =
                new Registration();


        registration.setAttendee(attendee);

        registration.setEvent(event);

        registration.setTicketQuantity(
                ticketQuantity
        );


        // Calculate total amount
        Double totalAmount =
                event.getPrice()
                        * ticketQuantity;


        registration.setTotalAmount(
                totalAmount
        );


        // Registration date
        registration.setRegistrationDate(
                LocalDateTime.now()
        );


        // Registration status
        registration.setStatus(
                RegistrationStatus.CONFIRMED
        );


        // Save registration
        return registrationRepository.save(
                registration
        );
    }


    // Get all registrations
    @Override
    public List<Registration>
    getAllRegistrations() {

        return registrationRepository.findAll();
    }


    // Get registration by ID
    @Override
    public Optional<Registration>
    getRegistrationById(
            Integer registrationId) {

        return registrationRepository
                .findById(registrationId);
    }


    // Get registrations by attendee
    @Override
    public List<Registration>
    getRegistrationsByAttendee(
            Integer attendeeId) {

        return registrationRepository
                .findByAttendeeAttendeeId(
                        attendeeId
                );
    }


    // Get registrations by event
    @Override
    public List<Registration>
    getRegistrationsByEvent(
            Integer eventId) {

        return registrationRepository
                .findByEventEventId(
                        eventId
                );
    }


    // Cancel registration
    @Override
    public Registration cancelRegistration(
            Integer registrationId) {

        Registration registration =
                registrationRepository
                        .findById(registrationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Registration not found with ID: "
                                                + registrationId
                                )
                        );


        registration.setStatus(
                RegistrationStatus.CANCELLED
        );


        return registrationRepository.save(
                registration
        );
    }
}