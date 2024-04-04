package com.guruprasad.trainticket.service;

import com.guruprasad.trainticket.dto.Route;
import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.repository.RouteRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route getRoute(TicketReservationPayload reservationPayload) {
        return findByFromAndToStation(reservationPayload.getFromStation(), reservationPayload.getToStation());
    }

    public Route findByFromAndToStation(String fromStation, String toStation) {
        return routeRepository.findRouteByStations(fromStation, toStation);
    }

    public void updateRoute(Route route) {
        routeRepository.save(route);
    }
}
