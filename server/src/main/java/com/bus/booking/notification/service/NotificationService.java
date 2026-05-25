package com.bus.booking.notification.service;

import com.bus.booking.notification.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void sendBookingConfirmation(
            Long userId,
            String email,
            String bookingReference
    );

    List<NotificationResponse>
    getUserNotifications(Long userId);

    void markNotificationAsRead(Long notificationId);
}