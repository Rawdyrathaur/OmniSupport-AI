package com.omnisupport.ticketservice.service;

import com.omnisupport.ticketservice.dto.TicketDto;
import com.omnisupport.ticketservice.enums.Priority;
import com.omnisupport.ticketservice.enums.Status;
import com.omnisupport.ticketservice.exc.NotFoundException;
import com.omnisupport.ticketservice.model.Ticket;
import com.omnisupport.ticketservice.repository.TicketRepository;
import com.omnisupport.ticketservice.request.ticket.TicketCreateRequest;
import com.omnisupport.ticketservice.request.ticket.TicketUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceNew {
    private final TicketRepository ticketRepository;
    private final TicketPriorityEngine priorityEngine;
    private final ModelMapper modelMapper;

    public TicketDto createTicket(TicketCreateRequest request) {
        log.info("Creating new support ticket for customer: {}", request.getCustomerId());

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID().toString())
                .title(request.getTitle())
                .description(request.getDescription())
                .customerId(request.getCustomerId())
                .priority(priorityEngine.analyzePriority(request.getTitle() + " " + request.getDescription()))
                .status(Status.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket created successfully with ID: {}", saved.getId());
        return modelMapper.map(saved, TicketDto.class);
    }

    public Page<TicketDto> getAllTickets(Pageable pageable) {
        log.debug("Fetching all tickets with pagination: {}", pageable);
        return ticketRepository.findAll(pageable).map(t -> modelMapper.map(t, TicketDto.class));
    }

    public TicketDto getTicketById(String id) {
        log.debug("Fetching ticket with ID: {}", id);
        Ticket ticket = findTicketById(id);
        return modelMapper.map(ticket, TicketDto.class);
    }

    public TicketDto updateTicket(String id, TicketUpdateRequest request) {
        log.info("Updating ticket with ID: {}", id);
        Ticket ticket = findTicketById(id);

        if (request.getTitle() != null) {
            ticket.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            ticket.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            ticket.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            ticket.setStatus(request.getStatus());
            if (request.getStatus() == Status.RESOLVED) {
                ticket.setResolvedAt(LocalDateTime.now());
            }
        }
        if (request.getAssignedAgentId() != null) {
            ticket.setAssignedAgentId(request.getAssignedAgentId());
        }

        ticket.setUpdatedAt(LocalDateTime.now());
        Ticket updated = ticketRepository.save(ticket);
        log.info("Ticket {} updated successfully", id);
        return modelMapper.map(updated, TicketDto.class);
    }

    public void deleteTicket(String id) {
        log.info("Deleting ticket with ID: {}", id);
        Ticket ticket = findTicketById(id);
        ticketRepository.delete(ticket);
        log.info("Ticket {} deleted successfully", id);
    }

    public Page<TicketDto> getTicketsByStatus(Status status, Pageable pageable) {
        log.debug("Fetching tickets with status: {}", status);
        return ticketRepository.findByStatus(status, pageable)
                .map(t -> modelMapper.map(t, TicketDto.class));
    }

    public Page<TicketDto> getTicketsByPriority(Priority priority, Pageable pageable) {
        log.debug("Fetching tickets with priority: {}", priority);
        return ticketRepository.findByPriority(priority, pageable)
                .map(t -> modelMapper.map(t, TicketDto.class));
    }

    public Page<TicketDto> getTicketsByAssignedAgent(String agentId, Pageable pageable) {
        log.debug("Fetching tickets assigned to agent: {}", agentId);
        return ticketRepository.findByAssignedAgentId(agentId, pageable)
                .map(t -> modelMapper.map(t, TicketDto.class));
    }

    public Page<TicketDto> getTicketsByCustomer(String customerId, Pageable pageable) {
        log.debug("Fetching tickets for customer: {}", customerId);
        return ticketRepository.findByCustomerId(customerId, pageable)
                .map(t -> modelMapper.map(t, TicketDto.class));
    }

    public TicketDto assignTicketToAgent(String ticketId, String agentId) {
        log.info("Assigning ticket {} to agent {}", ticketId, agentId);
        Ticket ticket = findTicketById(ticketId);
        ticket.setAssignedAgentId(agentId);
        ticket.setStatus(Status.IN_PROGRESS);
        ticket.setUpdatedAt(LocalDateTime.now());
        Ticket updated = ticketRepository.save(ticket);
        log.info("Ticket {} assigned to agent {} successfully", ticketId, agentId);
        return modelMapper.map(updated, TicketDto.class);
    }

    public TicketDto updateTicketStatus(String ticketId, Status status) {
        log.info("Updating ticket {} status to: {}", ticketId, status);
        Ticket ticket = findTicketById(ticketId);
        ticket.setStatus(status);
        if (status == Status.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }
        ticket.setUpdatedAt(LocalDateTime.now());
        Ticket updated = ticketRepository.save(ticket);
        log.info("Ticket {} status updated to {} successfully", ticketId, status);
        return modelMapper.map(updated, TicketDto.class);
    }

    protected Ticket findTicketById(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found with ID: " + id));
    }
}
