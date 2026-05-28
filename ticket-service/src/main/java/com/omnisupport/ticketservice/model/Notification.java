package com.omnisupport.ticketservice.model;

import com.omnisupport.ticketservice.enums.NotificationType;
import lombok.*;

import javax.persistence.*;

@Entity(name = "notifications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {
    private String message;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    private String recipientId;
    private String ticketId;
    private boolean isRead = false;
}
