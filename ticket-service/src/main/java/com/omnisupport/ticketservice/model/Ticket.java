package com.omnisupport.ticketservice.model;

import com.omnisupport.ticketservice.enums.Priority;
import com.omnisupport.ticketservice.enums.Status;
import lombok.*;

import javax.persistence.*;

@Entity(name = "tickets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends BaseEntity {
    private String title;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private Priority priority;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    private String assignedAgentId;
    private String customerId;
    private String category; // technical, billing, general
    
    @Column(name = "attachment_ids", columnDefinition = "TEXT")
    private String attachmentIds; // JSON array as string
    
    private java.time.LocalDateTime resolvedAt;
}
