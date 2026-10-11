package com.eventify.service;

import java.util.List;
import java.util.Optional;

import com.eventify.model.Notification;
import com.eventify.model.NotificationStatus;
import com.eventify.model.UserType;

public interface NotificationService {

    List<Notification> getAllNotifications();

    Optional<Notification> getNotificationById(
            Integer id
    );

    Notification createNotification(
            Notification notification
    );

    Notification updateNotification(
            Integer id,
            Notification updatedNotification
    );

    void deleteNotification(
            Integer id
    );

    List<Notification> getNotificationsByUser(
            Integer userId
    );

    List<Notification> getNotificationsByUserAndType(
            Integer userId,
            UserType userType
    );

    List<Notification> getNotificationsByUserAndStatus(
            Integer userId,
            NotificationStatus status
    );

    List<Notification> getNotificationsByUserType(
            UserType userType
    );
}