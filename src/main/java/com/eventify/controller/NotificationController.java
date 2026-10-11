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
import org.springframework.web.bind.annotation.RestController;

import com.eventify.model.Notification;
import com.eventify.model.NotificationStatus;
import com.eventify.model.UserType;
import com.eventify.service.NotificationService;

@RestController
@RequestMapping("/eventify/notifications")
public class NotificationController {

    private final NotificationService notificationService;


    public NotificationController(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }


    // Get all notifications
    @GetMapping
    public ResponseEntity<List<Notification>>
    getAllNotifications() {

        return ResponseEntity.ok(
                notificationService
                        .getAllNotifications()
        );
    }


    // Get notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification>
    getNotificationById(
            @PathVariable Integer id) {

        return notificationService
                .getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // Create notification
    @PostMapping
    public ResponseEntity<Notification>
    createNotification(
            @RequestBody Notification notification) {

        Notification createdNotification =
                notificationService
                        .createNotification(
                                notification
                        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdNotification);
    }


    // Update notification
    @PutMapping("/{id}")
    public ResponseEntity<Notification>
    updateNotification(
            @PathVariable Integer id,
            @RequestBody Notification updatedNotification) {

        Notification notification =
                notificationService
                        .updateNotification(
                                id,
                                updatedNotification
                        );

        return ResponseEntity.ok(notification);
    }


    // Delete notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteNotification(
            @PathVariable Integer id) {

        notificationService
                .deleteNotification(id);

        return ResponseEntity
                .noContent()
                .build();
    }


    // Get notifications by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>>
    getNotificationsByUser(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByUser(
                                userId
                        )
        );
    }


    // Get notifications by user and type
    @GetMapping("/user/{userId}/type/{userType}")
    public ResponseEntity<List<Notification>>
    getNotificationsByUserAndType(
            @PathVariable Integer userId,
            @PathVariable UserType userType) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByUserAndType(
                                userId,
                                userType
                        )
        );
    }


    // Get notifications by user and status
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Notification>>
    getNotificationsByUserAndStatus(
            @PathVariable Integer userId,
            @PathVariable NotificationStatus status) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByUserAndStatus(
                                userId,
                                status
                        )
        );
    }


    // Get notifications by user type
    @GetMapping("/type/{userType}")
    public ResponseEntity<List<Notification>>
    getNotificationsByUserType(
            @PathVariable UserType userType) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByUserType(
                                userType
                        )
        );
    }
}