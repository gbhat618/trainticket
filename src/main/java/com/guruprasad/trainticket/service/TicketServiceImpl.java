package com.guruprasad.trainticket.service;

import com.guruprasad.trainticket.dto.Ticket;
import com.guruprasad.trainticket.dto.TicketUpdatePayload;
import com.guruprasad.trainticket.repository.TicketRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket reserveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketFromPnr(String pnr) {
        return ticketRepository.findByPnrNumber(pnr);
    }

    public List<Ticket> listTicketsByTrainSection(int trainNumber, String section) {
        return ticketRepository.listTicketsByTrainSection(trainNumber, section);
    }

    public Ticket getTicketForSectionAndSeatNumber(long trainNumber, String section, int seat) {
        return ticketRepository.getTicketForSectionAndSeatNumber(trainNumber, section, seat);
    }

    public Ticket updateNewSeat(Ticket ticket, TicketUpdatePayload updatePayload) {
        ticket.setSection(updatePayload.getNewSection());
        ticket.setSeatNumber(updatePayload.getNewSeat());
        return ticketRepository.save(ticket);
    }
}
