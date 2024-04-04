package com.guruprasad.trainticket.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guruprasad.trainticket.dto.TicketUpdatePayload;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Set;

@Component
public class TrainUtils {

    ObjectMapper mapper;
    Set<String> validSections = Set.of("A", "B");
    Integer maxSeatCapacity = 60;

    public String generateNewPNR() {
        Random rand = new Random();
        return "555" + (rand.nextInt(9_000_000) + 1_000_000);
    }

    public ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = Jackson2ObjectMapperBuilder.json()
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).build();
        }

        return mapper;
    }

    public JsonNode validate(TicketUpdatePayload updatePayload) {
        StringBuilder sb = new StringBuilder();

        if (!validSections.contains(updatePayload.getNewSection())) {
            sb.append("Invalid section: " + updatePayload.getNewSection()).append(", ");
        }

        if (updatePayload.getNewSeat() > maxSeatCapacity) {
            sb.append("Seat number cannot be more than " + maxSeatCapacity + ", Requested seat was: " + updatePayload.getNewSeat());
        }

        if (!sb.isEmpty()) {
            ObjectNode resp = mapper.createObjectNode();
            resp.put("reason", sb.toString());
            return resp;
        }

        return null;
    }

}
