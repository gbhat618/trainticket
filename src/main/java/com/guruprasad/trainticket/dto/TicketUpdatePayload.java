package com.guruprasad.trainticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketUpdatePayload {
    String newSection;
    Integer newSeat;
}
