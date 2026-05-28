package com.omnisupport.notificationservice.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendNotificationRequest {
    private String userId;
    private String offerId;
    private String message;
}
