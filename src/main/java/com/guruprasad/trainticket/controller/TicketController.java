package com.guruprasad.trainticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.guruprasad.trainticket.dto.Ticket;
import com.guruprasad.trainticket.dto.TicketUpdatePayload;
import com.guruprasad.trainticket.service.TicketService;
import com.guruprasad.trainticket.utils.TrainUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @Autowired
    TrainUtils trainUtils;

    @GetMapping("/{pnrNumber}")
    public ResponseEntity<JsonNode> getTicket(@PathVariable("pnrNumber") String pnrNumber) throws JsonProcessingException {
        Ticket ticket = ticketService.getTicketFromPnr(pnrNumber);
        return ResponseEntity.ok().body(trainUtils.getMapper().convertValue(ticket, JsonNode.class));
    }

    @GetMapping("/{trainNumber}/{section}")
    public ResponseEntity<List<JsonNode>> listTickets(@PathVariable("trainNumber") Integer trainNumber, @PathVariable("section") String section) throws JsonProcessingException{
        List<Ticket> matchedTickets = ticketService.listTicketsByTrainSection(trainNumber, section);
        return ResponseEntity.ok().body(
                matchedTickets.stream()
                    .map(ticket -> trainUtils.getMapper().convertValue(ticket, JsonNode.class))
                    .collect(Collectors.toList())
        );
    }

    @PatchMapping("{pnr}")
    public ResponseEntity<JsonNode> changeSeat(@PathVariable("pnr") String pnrNumber, @RequestBody @Valid TicketUpdatePayload updatePayload) {

        JsonNode validate = trainUtils.validate(updatePayload);

        if (validate.has("reason")) {
            return ResponseEntity.badRequest().body(validate);
        }

        Ticket ticket = ticketService.getTicketFromPnr(pnrNumber);
        Ticket existingTicket = ticketService.getTicketForSectionAndSeatNumber(
            ticket.getRoute().getTrain().getTrainNumber(),
            updatePayload.getNewSection(),
            updatePayload.getNewSeat()
        );

        if (existingTicket != null) {
            ResponseEntity.badRequest().body("{\"reasons\": \"requested seat is already reserved\"}");
        }

        Ticket updated = ticketService.updateNewSeat(ticket, updatePayload);
        return ResponseEntity.ok().body(trainUtils.getMapper().convertValue(updated, JsonNode.class));
    }
}
