package com.eventify.service;

import java.util.List;
import java.util.Optional;

import com.eventify.model.Attendee;

public interface AttendeeService {

    // Get all attendees
    List<Attendee> getAllAttendees();

    // Get attendee by ID
    Optional<Attendee> getAttendeeById(Integer id);

    // Create new attendee
    Attendee createAttendee(Attendee attendee);

    // Update attendee
    Attendee updateAttendee(Integer id, Attendee updatedAttendee);

    // Delete attendee
    void deleteAttendee(Integer id);
}