package com.guruprasad.trainticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.guruprasad.trainticket.dto.Ticket;
import com.guruprasad.trainticket.dto.TicketUpdatePayload;

import java.util.List;

public interface TicketService {

    Ticket reserveTicket(Ticket ticket);

    Ticket getTicketFromPnr(String pnr);

    Ticket getTicketForSectionAndSeatNumber(long trainNumber, String section, int seat);

    List<Ticket> listTicketsByTrainSection(int trainNumber, String section);

    JsonNode updateNewSeatIfFree(String pnrNumber, TicketUpdatePayload updatePayload);
}
