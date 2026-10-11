package com.eventify.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventify.model.Feedback;
import com.eventify.service.FeedbackService;

@RestController
@RequestMapping("/eventify/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;


    public FeedbackController(
            FeedbackService feedbackService) {

        this.feedbackService = feedbackService;
    }


    // Get all feedback
    @GetMapping
    public ResponseEntity<List<Feedback>>
    getAllFeedback() {

        return ResponseEntity.ok(
                feedbackService.getAllFeedback()
        );
    }


    // Get feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback>
    getFeedbackById(
            @PathVariable Integer id) {

        return feedbackService
                .getFeedbackById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // Create feedback
    @PostMapping
    public ResponseEntity<Feedback>
    createFeedback(
            @RequestParam Integer attendeeId,
            @RequestParam Integer eventId,
            @RequestBody Feedback feedback) {

        Feedback createdFeedback =
                feedbackService.createFeedback(
                        attendeeId,
                        eventId,
                        feedback
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdFeedback);
    }


    // Update feedback
    @PutMapping("/{id}")
    public ResponseEntity<Feedback>
    updateFeedback(
            @PathVariable Integer id,
            @RequestBody Feedback updatedFeedback) {

        Feedback feedback =
                feedbackService.updateFeedback(
                        id,
                        updatedFeedback
                );

        return ResponseEntity.ok(feedback);
    }


    // Delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteFeedback(
            @PathVariable Integer id) {

        feedbackService.deleteFeedback(id);

        return ResponseEntity
                .noContent()
                .build();
    }


    // Get feedback by event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Feedback>>
    getFeedbackByEvent(
            @PathVariable Integer eventId) {

        return ResponseEntity.ok(
                feedbackService
                        .getFeedbackByEvent(eventId)
        );
    }


    // Get feedback by attendee
    @GetMapping("/attendee/{attendeeId}")
    public ResponseEntity<List<Feedback>>
    getFeedbackByAttendee(
            @PathVariable Integer attendeeId) {

        return ResponseEntity.ok(
                feedbackService
                        .getFeedbackByAttendee(attendeeId)
        );
    }
}