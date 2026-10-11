package com.eventify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventify.model.AccountStatus;
import com.eventify.model.Attendee;
import com.eventify.repository.AttendeeRepository;

@Service
public class AttendeeServiceImpl implements AttendeeService {

    private final AttendeeRepository attendeeRepository;

    public AttendeeServiceImpl(
            AttendeeRepository attendeeRepository) {

        this.attendeeRepository = attendeeRepository;
    }


   //get all attendee
    @Override
    public List<Attendee> getAllAttendees() {

        return attendeeRepository.findAll();
    }


    //get attendee by id
    @Override
    public Optional<Attendee> getAttendeeById(
            Integer id) {

        return attendeeRepository.findById(id);
    }


   //create new attendee
    @Override
    public Attendee createAttendee(
            Attendee attendee) {

        // Set default account status
        if (attendee.getAccountStatus() == null) {

            attendee.setAccountStatus(
                    AccountStatus.ACTIVE
            );
        }

        return attendeeRepository.save(attendee);
    }


   //update attendee
    @Override
    public Attendee updateAttendee(
            Integer id,
            Attendee updatedAttendee) {

        Attendee attendee = attendeeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Attendee not found with ID: "
                                        + id
                        )
                );

        attendee.setName(
                updatedAttendee.getName()
        );

        attendee.setEmail(
                updatedAttendee.getEmail()
        );

        attendee.setPhone(
                updatedAttendee.getPhone()
        );

        return attendeeRepository.save(attendee);
    }


   //delete attendee
    @Override
    public void deleteAttendee(
            Integer id) {

        if (!attendeeRepository.existsById(id)) {

            throw new RuntimeException(
                    "Attendee not found with ID: "
                            + id
            );
        }

        attendeeRepository.deleteById(id);
    }
}