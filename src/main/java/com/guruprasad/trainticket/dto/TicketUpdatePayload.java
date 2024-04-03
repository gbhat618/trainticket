package com.guruprasad.trainticket.dto;

import lombok.Getter;

@Getter
public class TicketUpdatePayload {
    String newSection;
    Integer newSeat;
}
