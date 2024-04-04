package com.guruprasad.trainticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-ticket")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<JsonNode> reserveTicket(@RequestBody @Valid TicketReservationPayload reservationPayload) throws JsonProcessingException {
        JsonNode response = reservationService.reseverAnyAvailableTicket(reservationPayload);

        if (response.has("pnr")) {
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
