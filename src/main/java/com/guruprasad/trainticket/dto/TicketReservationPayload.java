package com.guruprasad.trainticket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketReservationPayload {
    String fromStation;
    String toStation;
    Integer pricePaid;
    User user;
}
