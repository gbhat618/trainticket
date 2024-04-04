package com.guruprasad.trainticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.guruprasad.trainticket.dto.TicketReservationPayload;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    JsonNode reseverAnyAvailableTicket(TicketReservationPayload reservationPayload);

}
