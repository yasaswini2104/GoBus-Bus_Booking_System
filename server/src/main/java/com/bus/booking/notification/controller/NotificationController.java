package com.bus.booking.notification.controller;

import com.bus.booking.auth.entity.User;
import com.bus.booking.auth.repository.UserRepository;
import com.bus.booking.common.response.ApiResponse;
import com.bus.booking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<?>>
    getNotifications(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Notifications fetched successfully")
                        .data(
                                notificationService
                                        .getUserNotifications(user.getId())
                        )
                        .build()
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<?>>
    markAsRead(
            @PathVariable Long notificationId
    ) {

        notificationService
                .markNotificationAsRead(notificationId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Notification marked as read")
                        .build()
        );
    }
}