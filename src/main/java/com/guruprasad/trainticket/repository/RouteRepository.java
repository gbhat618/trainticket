package com.guruprasad.trainticket.repository;

import com.guruprasad.trainticket.dto.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT r FROM Route r WHERE r.fromStation=?1 AND r.toStation=?2")
    Route findRouteByStations(String fromStation, String toStation);
}
