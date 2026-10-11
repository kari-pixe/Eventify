package com.eventify.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventify.model.Notification;
import com.eventify.model.NotificationStatus;
import com.eventify.model.UserType;
import com.eventify.repository.NotificationRepository;

@Service
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationRepository notificationRepository;


    public NotificationServiceImpl(
            NotificationRepository notificationRepository) {

        this.notificationRepository =
                notificationRepository;
    }


    // Get all notifications
    @Override
    public List<Notification> getAllNotifications() {

        return notificationRepository.findAll();
    }


    // Get notification by ID
    @Override
    public Optional<Notification> getNotificationById(
            Integer id) {

        return notificationRepository.findById(id);
    }


    // Create notification
    @Override
    public Notification createNotification(
            Notification notification) {

        // Set default status
        if (notification.getStatus() == null) {

            notification.setStatus(
                    NotificationStatus.unread
            );
        }


        // Set sent date
        if (notification.getSentAt() == null) {

            notification.setSentAt(
                    LocalDateTime.now()
            );
        }


        return notificationRepository.save(
                notification
        );
    }


    // Update notification
    @Override
    public Notification updateNotification(
            Integer id,
            Notification updatedNotification) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found with ID: "
                                                + id
                                )
                        );


        notification.setMessage(
                updatedNotification.getMessage()
        );


        notification.setStatus(
                updatedNotification.getStatus()
        );


        return notificationRepository.save(
                notification
        );
    }


    // Delete notification
    @Override
    public void deleteNotification(
            Integer id) {

        if (!notificationRepository.existsById(id)) {

            throw new RuntimeException(
                    "Notification not found with ID: "
                            + id
            );
        }


        notificationRepository.deleteById(id);
    }


    // Get notifications by user
    @Override
    public List<Notification>
    getNotificationsByUser(
            Integer userId) {

        return notificationRepository
                .findByUserId(userId);
    }


    // Get notifications by user and type
    @Override
    public List<Notification>
    getNotificationsByUserAndType(
            Integer userId,
            UserType userType) {

        return notificationRepository
                .findByUserIdAndUserType(
                        userId,
                        userType
                );
    }


    // Get notifications by user and status
    @Override
    public List<Notification>
    getNotificationsByUserAndStatus(
            Integer userId,
            NotificationStatus status) {

        return notificationRepository
                .findByUserIdAndStatus(
                        userId,
                        status
                );
    }


    // Get notifications by user type
    @Override
    public List<Notification>
    getNotificationsByUserType(
            UserType userType) {

        return notificationRepository
                .findByUserType(userType);
    }
}