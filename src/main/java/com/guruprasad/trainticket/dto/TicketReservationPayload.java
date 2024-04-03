package com.guruprasad.trainticket.dto;

import lombok.Getter;

@Getter
public class TicketReservationPayload {
    String fromStation;
    String toStation;
    Integer pricePaid;
    User user;
}
