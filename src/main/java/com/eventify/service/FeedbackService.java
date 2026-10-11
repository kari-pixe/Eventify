package com.eventify.service;

import java.util.List;
import java.util.Optional;

import com.eventify.model.Feedback;

public interface FeedbackService {

    List<Feedback> getAllFeedback();

    Optional<Feedback> getFeedbackById(Integer id);

    Feedback createFeedback(
            Integer attendeeId,
            Integer eventId,
            Feedback feedback
    );

    Feedback updateFeedback(
            Integer id,
            Feedback updatedFeedback
    );

    void deleteFeedback(Integer id);

    List<Feedback> getFeedbackByEvent(
            Integer eventId
    );

    List<Feedback> getFeedbackByAttendee(
            Integer attendeeId
    );
}