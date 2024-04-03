package com.guruprasad.trainticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guruprasad.trainticket.constant.TicketStatus;
import com.guruprasad.trainticket.dto.Route;
import com.guruprasad.trainticket.dto.Ticket;
import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.dto.User;
import com.guruprasad.trainticket.service.RouteService;
import com.guruprasad.trainticket.service.TicketService;
import com.guruprasad.trainticket.service.UserService;
import com.guruprasad.trainticket.utils.TrainUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-ticket")
public class ReservationController {

    @Autowired
    TicketService ticketService;

    @Autowired
    RouteService routeService;

    @Autowired
    UserService userService;

    @Autowired
    TrainUtils trainUtils;

    @PostMapping
    public ResponseEntity<String> reserveTicket(@RequestBody @Valid TicketReservationPayload reservationPayload) throws JsonProcessingException {
        Route route = routeService.getRoute(reservationPayload);

        if (route.getPrice() != reservationPayload.getPricePaid()) {
            return ResponseEntity.badRequest().body("Need to pay exact price for the route; route price is: " + route.getPrice());
        }

        User purchasedBy = userService.getOrCreateUser(reservationPayload);
        Ticket ticket = new Ticket();

        if (route.getRemainingSeatSectionA() > 1) {
            route.setRemainingSeatSectionA(route.getRemainingSeatSectionA() - 1);
            routeService.updateRoute(route);
            ticket.setSection("A");

            // seat number
            int seatNumber = route.getRemainingSeatSectionA();
            ticket.setSeatNumber(seatNumber);
        } else if (route.getRemainingSeatSectionB() > 1) {
            route.setRemainingSeatSectionB(route.getRemainingSeatSectionB() - 1);
            routeService.updateRoute(route);
            ticket.setSection("B");

            // seat number
            int seatNumber = route.getRemainingSeatSectionB();
            ticket.setSeatNumber(seatNumber);
        } else {
            ResponseEntity.unprocessableEntity().body(
                "No Tickets are available, please raise request for amount refund within 30 days"
            );
        }

        ticket.setPnr(trainUtils.generateNewPNR());
        ticket.setRoute(route);
        ticket.setPurchasedBy(purchasedBy);
        ticket.setPassengerFullName(purchasedBy.getFirstName() + " " + purchasedBy.getLastName());
        ticket.setStatus(TicketStatus.RESERVED);

        /* currently the below attributes are not sent by the payload
            ticket.setPassengerAge();
            ticket.setPassengerSex();
        */

        Ticket saved = ticketService.reserveTicket(ticket);

        return ResponseEntity.ok().body(trainUtils.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(saved));
    }
}
