package com.bus.booking.notification.service.impl;

import com.bus.booking.auth.entity.User;
import com.bus.booking.auth.repository.UserRepository;
import com.bus.booking.notification.dto.response.NotificationResponse;
import com.bus.booking.notification.entity.Notification;
import com.bus.booking.notification.repository.NotificationRepository;
import com.bus.booking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Override
    public void sendBookingConfirmation(
            Long userId,
            String email,
            String bookingReference
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .title("Booking Confirmed")
                .message(
                        "Your booking has been confirmed. Booking Reference: "
                                + bookingReference
                )
                .notificationType("BOOKING")
                .isRead(false)
                .user(user)
                .build();

        notificationRepository.save(notification);

        sendEmail(
                email,
                "Your GoBus Booking Has Been Confirmed",
                "Dear " + user.getFullName() + ",\n\n" +
                "Your booking has been successfully confirmed.\n\n" +
                "Booking Details:\n" +
                "Booking Reference: " + bookingReference + "\n\n" +
                "Please retain this reference number for check-in and future correspondence.\n\n" +
                "If you have any questions or require assistance, please contact our support team.\n\n" +
                "Thank you for choosing GoBus.\n\n" +
                "Kind Regards,\n" +
                "GoBus Customer Support"
        );

        log.info(
                "Booking notification sent to: {}",
                email
        );
    }

    private void sendEmail(
            String to,
            String subject,
            String body
    ) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            log.info(
                    "Email sent successfully to: {}",
                    to
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to send email notification",
                    ex
            );
        }
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found")
                );

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse>
    getUserNotifications(Long userId) {

        return notificationRepository
                .findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notification ->
                        NotificationResponse.builder()
                                .id(notification.getId())
                                .title(notification.getTitle())
                                .message(notification.getMessage())
                                .notificationType(notification.getNotificationType())
                                .isRead(notification.getIsRead())
                                .createdAt(notification.getCreatedAt())
                                .build()
                )
                .collect(Collectors.toList());
    }
}