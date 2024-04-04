package com.guruprasad.trainticket.service;

import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.dto.User;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    User getOrCreateUser(TicketReservationPayload reservationPayload);

    void deleteByEmail(String email);

    void deleteById(Long id);
}
