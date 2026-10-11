package com.eventify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventify.model.Registration;

@Repository
public interface RegistrationRepository
        extends JpaRepository<Registration, Integer> {

    List<Registration> findByAttendeeAttendeeId(
            Integer attendeeId
    );

    List<Registration> findByEventEventId(
            Integer eventId
    );
    
    boolean existsByAttendee_AttendeeIdAndEvent_EventId(
            Integer attendeeId,
            Integer eventId
    );

    @Query("SELECT COALESCE(SUM(r.ticketQuantity), 0) FROM Registration r " +
           "WHERE r.event.eventId = :eventId AND r.status <> com.eventify.model.RegistrationStatus.CANCELLED")
    Integer sumActiveTicketQuantityByEvent(@Param("eventId") Integer eventId);
}