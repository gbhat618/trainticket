package com.guruprasad.trainticket.service;

import com.guruprasad.trainticket.dto.Route;
import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    @Autowired
    RouteRepository routeRepository;

    public Route getRoute(TicketReservationPayload reservationPayload) {
        return findByFromAndToStation(reservationPayload.getFromStation(), reservationPayload.getToStation());
    }

    Route findByFromAndToStation(String fromStation, String toStation) {
        return routeRepository.findRouteByStations(fromStation, toStation);
    }

    public void updateRoute(Route route) {
        routeRepository.save(route);
    }
}
