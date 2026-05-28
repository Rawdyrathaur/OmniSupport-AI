package com.omnisupport.ticketservice.request.notification;

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
