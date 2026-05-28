package com.omnisupport.ticketservice.request;

import com.omnisupport.ticketservice.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Priority is required")
    private Priority priority;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    private String category; // technical, billing, general
    
    private String assignedAgentId; // optional, can be assigned later
}
