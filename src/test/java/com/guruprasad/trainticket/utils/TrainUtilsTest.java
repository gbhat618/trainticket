package com.guruprasad.trainticket.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.guruprasad.trainticket.dto.TicketUpdatePayload;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TrainUtilsTest {

    private final TrainUtils trainUtils = new TrainUtils();

    @Test
    void test_GenerateNewPNR_() {
        assertThat(trainUtils.generateNewPNR().length()).isEqualTo(10);
    }

    @Test
    void test_validate_correct_payload_returns_null() {
        TicketUpdatePayload ticketUpdatePayload = new TicketUpdatePayload("A", 55);
        JsonNode validate = trainUtils.validate(ticketUpdatePayload);
        assertThat(validate).isNull();
    }

    @Test
    void test_validate_Invalid_Section_And_Invalid_Seat() {
        TicketUpdatePayload ticketUpdatePayload = new TicketUpdatePayload("C", 62);
        JsonNode validate = trainUtils.validate(ticketUpdatePayload);
        assertThat(validate.get("reason").asText()).isEqualTo("Invalid section: C, Seat number cannot be more than 60, Requested seat was: 62");
    }
}