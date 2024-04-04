package com.guruprasad.trainticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guruprasad.trainticket.dto.Ticket;
import com.guruprasad.trainticket.dto.TicketUpdatePayload;
import com.guruprasad.trainticket.repository.TicketRepository;
import com.guruprasad.trainticket.utils.TrainUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TrainUtils trainUtils;

    public TicketServiceImpl(TicketRepository ticketRepository, TrainUtils trainUtils) {
        this.ticketRepository = ticketRepository;
        this.trainUtils = trainUtils;
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

    public JsonNode updateNewSeatIfFree(String pnrNumber, TicketUpdatePayload updatePayload) {
        Ticket ticket = this.getTicketFromPnr(pnrNumber);

        Ticket existingTicket = this.getTicketForSectionAndSeatNumber(
            ticket.getRoute().getTrain().getTrainNumber(),
            updatePayload.getNewSection(),
            updatePayload.getNewSeat()
        );

        if (existingTicket != null) {
            ObjectNode response = trainUtils.getMapper().createObjectNode();
            response.put("reason", "requested seat is already reserved, please request again with different change of seat");
            return response;
        }

        ticket.setSection(updatePayload.getNewSection());
        ticket.setSeatNumber(updatePayload.getNewSeat());
        Ticket saved = ticketRepository.save(ticket);

        return trainUtils.getMapper().convertValue(saved, JsonNode.class);
    }
}
