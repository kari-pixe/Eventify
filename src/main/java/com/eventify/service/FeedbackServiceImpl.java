package com.eventify.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventify.model.Attendee;
import com.eventify.model.Event;
import com.eventify.model.Feedback;
import com.eventify.repository.AttendeeRepository;
import com.eventify.repository.EventRepository;
import com.eventify.repository.FeedbackRepository;
import com.eventify.repository.RegistrationRepository;

@Service
public class FeedbackServiceImpl
        implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    private final AttendeeRepository attendeeRepository;

    private final EventRepository eventRepository;

    private final RegistrationRepository registrationRepository;


    public FeedbackServiceImpl(
            FeedbackRepository feedbackRepository,
            AttendeeRepository attendeeRepository,
            EventRepository eventRepository,
            RegistrationRepository registrationRepository) {

        this.feedbackRepository = feedbackRepository;
        this.attendeeRepository = attendeeRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }


    // Get all feedback
    @Override
    public List<Feedback> getAllFeedback() {

        return feedbackRepository.findAll();
    }


    // Get feedback by ID
    @Override
    public Optional<Feedback> getFeedbackById(
            Integer id) {

        return feedbackRepository.findById(id);
    }


    // Create feedback
    @Override
    public Feedback createFeedback(
            Integer attendeeId,
            Integer eventId,
            Feedback feedback) {

        // 1. Check attendee exists
        Attendee attendee =
                attendeeRepository
                        .findById(attendeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attendee not found with ID: "
                                                + attendeeId
                                )
                        );


        // 2. Check event exists
        Event event =
                eventRepository
                        .findById(eventId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Event not found with ID: "
                                                + eventId
                                )
                        );


        // 3. Validate rating
        if (feedback.getRating() == null
                || feedback.getRating() < 1
                || feedback.getRating() > 5) {

            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }


        // 4. Check attendee registered for event
        boolean registered =
                registrationRepository
                        .existsByAttendee_AttendeeIdAndEvent_EventId(
                                attendeeId,
                                eventId
                        );

        if (!registered) {

            throw new RuntimeException(
                    "Attendee has not registered for this event"
            );
        }


        // 5. Check event has ended
        if (event.getEndDate() == null) {

            throw new RuntimeException(
                    "Event end date is not available"
            );
        }

        if (event.getEndDate().isAfter(
                LocalDateTime.now())) {

            throw new RuntimeException(
                    "Feedback can only be submitted after the event has ended"
            );
        }


        // 6. Check duplicate feedback
        boolean alreadySubmitted =
                feedbackRepository
                        .existsByEvent_EventIdAndAttendee_AttendeeId(
                                eventId,
                                attendeeId
                        );

        if (alreadySubmitted) {

            throw new RuntimeException(
                    "Attendee has already submitted feedback for this event"
            );
        }


        // 7. Set attendee
        feedback.setAttendee(attendee);


        // 8. Set event
        feedback.setEvent(event);


        // 9. Set submission date
        if (feedback.getSubmittedAt() == null) {

            feedback.setSubmittedAt(
                    LocalDateTime.now()
            );
        }


        // 10. Save feedback
        return feedbackRepository.save(feedback);
    }


    // Update feedback
    @Override
    public Feedback updateFeedback(
            Integer id,
            Feedback updatedFeedback) {

        Feedback feedback =
                feedbackRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Feedback not found with ID: "
                                                + id
                                )
                        );


        // Validate rating
        if (updatedFeedback.getRating() == null
                || updatedFeedback.getRating() < 1
                || updatedFeedback.getRating() > 5) {

            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }


        // Update rating
        feedback.setRating(
                updatedFeedback.getRating()
        );


        // Update review
        feedback.setReviewText(
                updatedFeedback.getReviewText()
        );


        return feedbackRepository.save(feedback);
    }


    // Delete feedback
    @Override
    public void deleteFeedback(
            Integer id) {

        if (!feedbackRepository.existsById(id)) {

            throw new RuntimeException(
                    "Feedback not found with ID: "
                            + id
            );
        }

        feedbackRepository.deleteById(id);
    }


    // Get feedback by event
    @Override
    public List<Feedback> getFeedbackByEvent(
            Integer eventId) {

        return feedbackRepository
                .findByEvent_EventId(eventId);
    }


    // Get feedback by attendee
    @Override
    public List<Feedback> getFeedbackByAttendee(
            Integer attendeeId) {

        return feedbackRepository
                .findByAttendee_AttendeeId(attendeeId);
    }
}