package com.omnisupport.ticketservice.request;

import com.omnisupport.ticketservice.enums.Priority;
import com.omnisupport.ticketservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateRequest {
    @NotBlank(message = "Ticket ID is required")
    private String id;
    
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private String assignedAgentId;
    private String category;
}
