package com.eventify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventify.model.Feedback;

@Repository
public interface FeedbackRepository
        extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByEvent_EventId(Integer eventId);

    List<Feedback> findByAttendee_AttendeeId(Integer attendeeId);

    List<Feedback> findByEvent_EventIdAndAttendee_AttendeeId(
            Integer eventId,
            Integer attendeeId
    );

    boolean existsByEvent_EventIdAndAttendee_AttendeeId(
            Integer eventId,
            Integer attendeeId
    );
}