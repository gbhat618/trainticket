package com.guruprasad.trainticket.service;

import com.guruprasad.trainticket.dto.Route;
import com.guruprasad.trainticket.dto.TicketReservationPayload;

public interface RouteService {

    Route getRoute(TicketReservationPayload ticketReservationPayload);

    Route findByFromAndToStation(String fromStation, String toStation);

    void updateRoute(Route route);
}
