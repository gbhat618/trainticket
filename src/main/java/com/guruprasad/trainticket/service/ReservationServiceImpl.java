package com.guruprasad.trainticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guruprasad.trainticket.constant.TicketStatus;
import com.guruprasad.trainticket.dto.Route;
import com.guruprasad.trainticket.dto.Ticket;
import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.dto.User;
import com.guruprasad.trainticket.utils.TrainUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class ReservationServiceImpl implements ReservationService {

    private final TicketService ticketService;
    private final RouteService routeService;
    private final UserService userService;
    private final TrainUtils trainUtils;

    public ReservationServiceImpl(TicketService ticketService, RouteService routeService, UserService userService, TrainUtils trainUtils) {
        this.ticketService = ticketService;
        this.routeService = routeService;
        this.userService = userService;
        this.trainUtils = trainUtils;
    }

    @Override
    public JsonNode reseverAnyAvailableTicket(TicketReservationPayload reservationPayload) {
        Route route = routeService.getRoute(reservationPayload);

        if (route.getPrice() != reservationPayload.getPricePaid()) {
            ObjectNode response = trainUtils.getMapper().createObjectNode();
            response.put("reason", "Need to pay exact price for the route; route price is: " + route.getPrice());
            return response;
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
            ObjectNode response = trainUtils.getMapper().createObjectNode();
            response.put("reason", "No Tickets are available, please raise request for amount refund within 30 days");
            return response;
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

        return trainUtils.getMapper().convertValue(saved, JsonNode.class);
    }
}
