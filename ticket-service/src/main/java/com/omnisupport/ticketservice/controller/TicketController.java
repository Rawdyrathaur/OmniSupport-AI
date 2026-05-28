package com.omnisupport.ticketservice.controller;

import com.omnisupport.ticketservice.dto.TicketDto;
import com.omnisupport.ticketservice.request.TicketCreateRequest;
import com.omnisupport.ticketservice.request.TicketUpdateRequest;
import com.omnisupport.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST API Controller for Support Ticket Management
 * Handles CRUD operations and ticket lifecycle management
 */
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    /**
     * Create a new support ticket
     * POST /api/v1/tickets
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    ResponseEntity<TicketDto> createTicket(@Valid @RequestBody TicketCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(ticketService.createTicket(request), TicketDto.class));
    }

    /**
     * Get all tickets (paginated, with filters)
     * GET /api/v1/tickets
     */
    @GetMapping
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN') or hasRole('CUSTOMER')")
    ResponseEntity<List<TicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ticketService.getAllTickets(page, size).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }

    /**
     * Get ticket by ID
     * GET /api/v1/tickets/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN') or hasRole('CUSTOMER')")
    ResponseEntity<TicketDto> getTicketById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(ticketService.getTicketById(id), TicketDto.class));
    }

    /**
     * Get tickets by status
     * GET /api/v1/tickets/status/{status}
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    ResponseEntity<List<TicketDto>> getTicketsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }

    /**
     * Get tickets by priority
     * GET /api/v1/tickets/priority/{priority}
     */
    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    ResponseEntity<List<TicketDto>> getTicketsByPriority(@PathVariable String priority) {
        return ResponseEntity.ok(ticketService.getTicketsByPriority(priority).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }

    /**
     * Get tickets assigned to an agent
     * GET /api/v1/tickets/agent/{agentId}
     */
    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    ResponseEntity<List<TicketDto>> getTicketsByAgent(@PathVariable String agentId) {
        return ResponseEntity.ok(ticketService.getTicketsByAgent(agentId).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }

    /**
     * Get customer's tickets
     * GET /api/v1/tickets/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    ResponseEntity<List<TicketDto>> getTicketsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(ticketService.getTicketsByCustomer(customerId).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class)).toList());
    }

    /**
     * Update ticket
     * PUT /api/v1/tickets/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    ResponseEntity<TicketDto> updateTicket(@PathVariable String id,
                                           @Valid @RequestBody TicketUpdateRequest request) {
        request.setId(id);
        return ResponseEntity.ok(modelMapper.map(ticketService.updateTicket(request), TicketDto.class));
    }

    /**
     * Assign ticket to agent
     * POST /api/v1/tickets/{id}/assign
     */
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<TicketDto> assignTicket(@PathVariable String id,
                                           @RequestParam String agentId) {
        return ResponseEntity.ok(modelMapper.map(ticketService.assignTicket(id, agentId), TicketDto.class));
    }

    /**
     * Update ticket status
     * POST /api/v1/tickets/{id}/status
     */
    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    ResponseEntity<TicketDto> updateTicketStatus(@PathVariable String id,
                                                 @RequestParam String status) {
        return ResponseEntity.ok(modelMapper.map(ticketService.updateStatus(id, status), TicketDto.class));
    }

    /**
     * Delete ticket (soft delete)
     * DELETE /api/v1/tickets/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deleteTicket(@PathVariable String id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok().build();
    }
}
