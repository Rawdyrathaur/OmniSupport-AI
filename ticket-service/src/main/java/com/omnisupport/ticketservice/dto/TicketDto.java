package com.omnisupport.ticketservice.dto;

import com.omnisupport.ticketservice.enums.Priority;
import com.omnisupport.ticketservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
    private String id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private String assignedAgentId;
    private String customerId;
    private String category;
    private String attachmentIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
}
