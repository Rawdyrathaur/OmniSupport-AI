package com.omnisupport.ticketservice.request.ticket;

import com.omnisupport.ticketservice.enums.Priority;
import com.omnisupport.ticketservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketUpdateRequest {
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private String assignedAgentId;
}
