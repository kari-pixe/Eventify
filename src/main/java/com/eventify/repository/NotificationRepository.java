package com.eventify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventify.model.Notification;
import com.eventify.model.NotificationStatus;
import com.eventify.model.UserType;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserId(
            Integer userId
    );

    List<Notification> findByUserIdAndUserType(
            Integer userId,
            UserType userType
    );

    List<Notification> findByUserIdAndStatus(
            Integer userId,
            NotificationStatus status
    );

    List<Notification> findByUserType(
            UserType userType
    );
}