package com.bus.booking.notification.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private String notificationType;

    private Boolean isRead;
}